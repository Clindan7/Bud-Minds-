import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AppConstants } from 'src/app/app.constants';

@Injectable({
  providedIn: 'root'
})
export class ValuationServiceService {

  constructor(private http: HttpClient) { }

  private API_ENDPOINT$ = AppConstants.API_ENDPOINT;


  addScore(data: any): Observable<any> {   
    let string =`${this.API_ENDPOINT$}score`;
    return this.http.post(string,data); 
  }
}
