import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
const ACCESS_TOKEN = 'accessToken';
const REFRESH_TOKEN = 'refreshToken';
import { AppConstants } from "../../app.constants";

@Injectable({
  providedIn: 'root'
})
export class LoginServiceService {

  constructor(private http:HttpClient,private router: Router) { }

  private API_ENDPOINT$ = AppConstants.API_ENDPOINT;

  /**
   * Method to check whether the user is LoggedIn or not
   * @returns
   */
  isLoggedIn(): boolean {
    return !!this.getAccessToken();
  }

  /**
   * Method to get ACESS_TOKEN from localStorage
   * @returns
   */
  getAccessToken(): string | null {
    return localStorage.getItem(ACCESS_TOKEN);
  }

  /**
   * Method to save ACCESS_TOKEN to localStorage
   * @param strAccessToken
   */
  saveAccessToken(strAccessToken: string) {
    localStorage.setItem(ACCESS_TOKEN, strAccessToken);
  }

  /**
   * Method to get REFRESH_TOKEN from localStorage
   * @returns
   */
  getRefreshToken(): string | null {
    return localStorage.getItem(REFRESH_TOKEN);
  }

  /**
   * Method to save REFRESH_TOKEN to localStorage
   * @param strRefreshToken
   */
  saveRefreshToken(strRefreshToken: string) {
    localStorage.setItem(REFRESH_TOKEN, strRefreshToken);
  }

  /**
   * Method to send REFRESH_TOKEN to get new ACCESS_TOKEN
   * @returns
   */
  refreshLogin(): Observable<any> {
    return this.http.put(
      this.API_ENDPOINT$+'login',
      this.getRefreshToken()
    );
  }

  /**
   * Method for user login
   * @param obj
   * @returns
   */
  userLogin(obj:any): Observable<any> {
    return this.http
      .post(this.API_ENDPOINT$+'login', obj)
      .pipe(
        // login successful if there is an accessToken in the response
        map((user) => {
          if (Object.keys(user).length && 'accessToken' in user) {
            localStorage.setItem('userId',btoa(user['userId']));
            localStorage.setItem('userId',btoa(user['userId']));
            localStorage.setItem(REFRESH_TOKEN, user['refreshToken'].value);
            localStorage.setItem(ACCESS_TOKEN, user['accessToken']?.['value']);
            localStorage.setItem('userRole',btoa(user['userRole']));
          }
          return user;
        })
      );
  }

  /**
   * Method to clear localStorage
   */
  logout() {
    localStorage.clear();
    this.router.navigateByUrl('/login');
  }

}


