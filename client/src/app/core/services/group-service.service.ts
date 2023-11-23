import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AppConstants } from 'src/app/app.constants';

@Injectable({
  providedIn: 'root'
})
export class GroupServiceService {

  constructor(private http: HttpClient) {}

  private API_ENDPOINT$ = AppConstants.API_ENDPOINT;

  fetchUser(): Observable<any> {
    return this.http.get(this.API_ENDPOINT$+'users/profile');
  }

  getGroups(page:any,limit:any,param?:any): Observable<any> {
    if (page == null){
      page = 1;
    }

    let string =`${this.API_ENDPOINT$}group?limit=${limit}&page=${page}`
    if(page<1){
      string =`${this.API_ENDPOINT$}group?&page=1`
    }
    if(param){
      if(param.batchName)
      string=string+`&joinerBatchId=${param.batchName}`

      if(param.groupName)
      string = string +`&groupSearch=${param.groupName}`
    }
    return this.http.get(string);
  }

  searchBatch(): Observable<any> {
    return this.http.get(this.API_ENDPOINT$+'batch/all');
  }

  deleteGroup(groupId:any): Observable<any> {
    return this.http.delete(this.API_ENDPOINT$+'group/'+groupId);
  }

  changePassword(data: any): Observable<any> {
    return this.http.put(this.API_ENDPOINT$+'users/changePassword',data);
  }

  addGroup(data: any): Observable<any> {
    return this.http.post(this.API_ENDPOINT$+'group',data);
  }

  editGroup(data:any,groupId: any): Observable<any> {
    return this.http.put(this.API_ENDPOINT$+'group/'+groupId, data);
  }

  viewGroup(groupId: any): Observable<any> {
    return this.http.get(this.API_ENDPOINT$+'group/'+groupId);
  }
}
