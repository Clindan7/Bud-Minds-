import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AppConstants } from 'src/app/app.constants';

@Injectable({
  providedIn: 'root'
})
export class TrainerServiceService {

  page:any;
  limit:any;

  constructor(private http: HttpClient) {}


  private API_ENDPOINT$ = AppConstants.API_ENDPOINT;

  fetchUser(): Observable<any> {
    return this.http.get(this.API_ENDPOINT$+'users/profile');
  }

  listUsers(page:any,limit:any,param?:any): Observable<any> {
    let string =`${this.API_ENDPOINT$}trainers?&page=${page}`
    if(page<1){
      string =`${this.API_ENDPOINT$}trainers?&page=1`
    }
    if(param){
      if(param.employeeId)
      string=string+`&employeeId=${param.employeeId}`

      if(param.status)
      string=string+`&status=${param.status}`

      if(param.name)
      string = string +`&search=${param.name}`
    }
    return this.http.get(string);
  }

  searchUser(): Observable<any> {
    return this.http.get(this.API_ENDPOINT$+'trainers/firstName');
  }
  delete(trainerId:any): Observable<any> {
    return this.http.delete(this.API_ENDPOINT$+'trainers/'+trainerId);
  }

  changePassword(data: any): Observable<any> {
    return this.http.put(this.API_ENDPOINT$+'users/changePassword',data);
  }

  register(data: any): Observable<any> {
    return this.http.post(this.API_ENDPOINT$+'trainers/',data);
  }

  update(data:any,trainerId: any): Observable<any> {
    return this.http.put(this.API_ENDPOINT$+'trainers/'+trainerId, data);
  }

  view(trainerId: any): Observable<any> {
    return this.http.get(this.API_ENDPOINT$+'trainers/'+trainerId);
  }

  importCSV(file:any): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('file',file );
    return this.http.post(this.API_ENDPOINT$+'trainer/csvImport', formData);
  }

  exportCSV(): Observable<any> {
    return this.http.get(this.API_ENDPOINT$+'trainer/csvExport/file.csv',{observe: 'body', responseType: 'text' as 'json'});
  }

  setPageNum(pageNum:any){
    this.page=pageNum;
  }

}
