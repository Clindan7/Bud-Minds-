import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { AppConstants } from 'src/app/app.constants';

@Injectable({
  providedIn: 'root'
})
export class ManagerServiceService {

  page:any;
  limit:any;

  constructor(private http: HttpClient, private route: ActivatedRoute) {}

  private API_ENDPOINT$ = AppConstants.API_ENDPOINT;

  fetchUser(): Observable<any> {
    return this.http.get(this.API_ENDPOINT$+'users/profile');
  }

  listUsers(page:any,limit:any,param?:any): Observable<any> {
    let string =`${this.API_ENDPOINT$}managers?&page=${page}`
    if(page<1){
      string =`${this.API_ENDPOINT$}managers?&page=1`
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
    return this.http.get(this.API_ENDPOINT$+'managers/firstName');
  }
  delete(managerId:any): Observable<any> {
    return this.http.delete(this.API_ENDPOINT$+'managers/'+managerId);
  }

  changePassword(data: any): Observable<any> {
    return this.http.put(this.API_ENDPOINT$+'users/changePassword',data);
  }

  register(data: any): Observable<any> {
    return this.http.post(this.API_ENDPOINT$+'managers/',data);
  }

  update(data:any,managerId: any): Observable<any> {
    return this.http.put(this.API_ENDPOINT$+'managers/'+managerId, data);
  }

  view(managerId: any): Observable<any> {
    return this.http.get(this.API_ENDPOINT$+'managers/'+managerId);
  }

  importCSV(file:any): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('file',file );
    return this.http.post(this.API_ENDPOINT$+'manager/csvImport', formData);
  }

  exportCSV(): Observable<any> {
    return this.http.get(this.API_ENDPOINT$+'manager/csvExport/file.csv',{observe: 'body', responseType: 'text' as 'json'});
  }

setPageNum(pageNum:any){

  this.page=pageNum;

}

}
