import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AppConstants } from 'src/app/app.constants';

@Injectable({
  providedIn: 'root'
})
export class TrainingServiceService {

  page:any;
  limit:any;

  constructor(private http: HttpClient) {}


  private API_ENDPOINT$ = AppConstants.API_ENDPOINT;

  fetchUser(): Observable<any> {
    return this.http.get(this.API_ENDPOINT$+'users/profile');
  }

  getTraining(page:any,limit:any,param?:any): Observable<any> {
    let string =`${this.API_ENDPOINT$}training?limit=${limit}&page=${page}`
    if(page<1){
      string =`${this.API_ENDPOINT$}training?&page=1`
    }
    if(param){
      if(param.search){
        string=string+`&search=${param.search}`
      }
      if(param.departmentId){
      string=string+`&departmentId=${param.departmentId}`
      }

      if(param.technologyId){
      string = string +`&technologyId=${param.technologyId}`
      }
    }
    return this.http.get(string);
  }

  getTechnology(): Observable<any> {
    return this.http.get(this.API_ENDPOINT$+'technology');
  }

  addTechnology(data: any): Observable<any> {
    return this.http.post(this.API_ENDPOINT$+'technology/',data);
  }

  deleteTechnology(technologyId:any): Observable<any> {
    return this.http.delete(this.API_ENDPOINT$+'technology/'+technologyId);
  }

  deleteTraining(trainingId:any): Observable<any> {
    return this.http.delete(this.API_ENDPOINT$+'training/'+trainingId);
  }

  changePassword(data: any): Observable<any> {
    return this.http.put(this.API_ENDPOINT$+'users/changePassword',data);
  }

  addTraining(data: any): Observable<any> {
    return this.http.post(this.API_ENDPOINT$+'training/',data);
  }

  editTraining(data:any,trainingId: any): Observable<any> {
    return this.http.put(this.API_ENDPOINT$+'training/'+trainingId, data);
  }

  viewTraining(trainingId: any): Observable<any> {
    return this.http.get(this.API_ENDPOINT$+'training/'+trainingId);
  }

  setPageNum(pageNum:any){
    this.page=pageNum;
  }

}
