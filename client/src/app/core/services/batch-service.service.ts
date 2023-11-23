import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AppConstants } from 'src/app/app.constants';

@Injectable({
  providedIn: 'root'
})
export class BatchServiceService {

  constructor(private http:HttpClient) { }

  private API_ENDPOINT$ = AppConstants.API_ENDPOINT;

  fetchUser(): Observable<any> {
    return this.http.get(this.API_ENDPOINT$+'users/profile');
  }

  getBatches(): Observable<any> {
    return this.http.get(this.API_ENDPOINT$+'batch/batchName');
  }

  findBatches(page:any,limit:any,param?:any): Observable<any> {
    let string =`${this.API_ENDPOINT$}batch?limit=${limit}&page=${page}`
    if(param?.batch){
      string=string+`&search=${param.batch}`
    }
    return this.http.get(string);
  }

  viewBatch(batchId:any): Observable<any> {
    return this.http.get(this.API_ENDPOINT$+'batch/'+batchId);
  }

  addBatch(data: any): Observable<any> {
    return this.http.post(this.API_ENDPOINT$+'batch',data);
  }

  editBatch(data:any,batchId: any): Observable<any> {
    return this.http.put(this.API_ENDPOINT$+'batch/'+batchId, data);
  }
  deleteBatch(batchId:any): Observable<any> {
    return this.http.delete(this.API_ENDPOINT$+'batch/'+batchId);
  }

}
