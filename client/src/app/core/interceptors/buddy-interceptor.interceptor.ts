import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse,
} from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { Router} from '@angular/router';
import { catchError, filter, finalize, switchMap, take } from 'rxjs/operators';
import { LoginServiceService } from '../services/login-service.service';


@Injectable()
export class BuddyInterceptorInterceptor implements HttpInterceptor {
  // if refresh access token is send or not
  isRefreshing = false;
  private refreshTokenSubject$: BehaviorSubject<any> = new BehaviorSubject<any>(
    null
  );

  constructor(private loginService: LoginServiceService, private router: Router) {}

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    // if access token in the local storage append to same in incomming request
    if (this.loginService.getAccessToken())
      request = this.addToken(request, this.loginService.getAccessToken());
    return next.handle(request).pipe(
      catchError((objError: any) => {
        if (
          objError instanceof HttpErrorResponse &&
          (objError.status === 401 || objError.status === 403)
        )
          return this.handle401Error(request, next);
         else {
          return throwError(objError);
        }
      })
    );
  }

  /**
   * Add token to all request
   * @param objRequest http request
   * @param access_token access token
   * @returns headers
   */
  private addToken(
    objRequest: HttpRequest<any>,
    access_token: string | null
  ): HttpRequest<any> {
    //appends only content-type application/json; for login requests
    if (objRequest.url.includes('/login') || objRequest.url.includes('/users/forgotPassword') || objRequest.url.includes('/users/resetPassword')) {
      return objRequest.clone({
        setHeaders: {
          'Content-Type': `application/json; charset=utf-8`,
        },
      });
    }

    else if (objRequest.url.includes('manager/csvImport') || objRequest.url.includes('mentor/csvImport') || objRequest.url.includes('trainer/csvImport') || objRequest.url.includes('trainee/csvImport')) {
      return objRequest.clone({
        setHeaders: {
          Authorization: 'BuddyManagement '+access_token
        },
      });
    }

    //appends content-type application/json; and token
    else {

      if ((objRequest.method == 'POST' || objRequest.method == 'PUT' || objRequest.method == 'PATCH')){
        objRequest = objRequest.clone({
          withCredentials: true,
          setHeaders: {
            Authorization: `BuddyManagement ${access_token}`
          }
        });
      }

        return objRequest.clone({
        setHeaders: {
          'Content-Type': `application/json; charset=utf-8`,
          Authorization: `BuddyManagement ${access_token}`,
        },
      });

    }
  }

  //Handling 401 Error response
  private handle401Error(
    objRequest: HttpRequest<any>,
    objNext: HttpHandler
  ): Observable<HttpEvent<any>> {
    if (!this.isRefreshing) {
      this.isRefreshing = true;

      //clearing the refresh token subject
      this.refreshTokenSubject$.next(null);

      // service to generate new access token by sending refresh token to server
      return this.loginService.refreshLogin().pipe(
        switchMap((objToken: any) => {
          this.isRefreshing = false;
          this.loginService.saveAccessToken(
            objToken.accessToken['value']
          );
          this.loginService.saveRefreshToken(
            objToken.refreshToken['value']
          );
          this.refreshTokenSubject$.next(objToken.accessToken['value']);
          return objNext.handle(
            this.addToken(objRequest, objToken.accessToken['value'])
          );
        }),
        catchError((objError: any) => {
          this.isRefreshing = false;
          this.loginService.logout();
          const err = throwError(() => objError);
          return err;
        })
      );
    } else {
      /**
       * if refreshing (getting new access token) is in progress then block the upcoming all other request
       * till a new valid acess token is emitting by refresh token subject
       */
      return this.refreshTokenSubject$.pipe(
        filter((strToken) => strToken != null),
        take(1),
        switchMap((jwt) => objNext.handle(this.addToken(objRequest, jwt))),
        finalize(() => {
          console.log('finished refresh token subject');
        })
      );
    }
  }
}
