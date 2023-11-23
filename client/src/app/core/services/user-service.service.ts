import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AppConstants } from "../../app.constants";

@Injectable({
  providedIn: 'root',
})
export class UserServiceService {
  constructor(private http: HttpClient) {}

  private API_ENDPOINT$ = AppConstants.API_ENDPOINT;

  fetchUser(): Observable<any> {
    return this.http.get(this.API_ENDPOINT$+'users/profile');
  }
  updateProfile(form: any): Observable<any> {
    return this.http.put(this.API_ENDPOINT$+'users/profile', form);
  }
  forgotPassword(data: any): Observable<any> {
    return this.http.post(this.API_ENDPOINT$+'users/forgotPassword',data );
  }

  resetPassword(data: any): Observable<any> {
    return this.http.put(this.API_ENDPOINT$+'users/resetPassword',data);
  }

  validateToken(token: any): Observable<any> {
    return this.http.post(this.API_ENDPOINT$+'users/token/verify',token);
  }

  changePassword(data: any): Observable<any> {
    return this.http.put(this.API_ENDPOINT$+'users/changePassword',data);
  }


}
