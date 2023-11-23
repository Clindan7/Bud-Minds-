import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { AppConstants } from 'src/app/app.constants';

@Injectable({
  providedIn: 'root'
})
export class MentorResourceAllocationService {

  page:any;
  limit:any;
  constructor(private http: HttpClient, private route: ActivatedRoute) { }
  private API_ENDPOINT$ = AppConstants.API_ENDPOINT;

  assignedResources(page:any,limit:any,mentorId:any,param?:any): Observable<any> {
    let string =`${this.API_ENDPOINT$}mentors/resource-list?mentorId=${mentorId}&page=${page}`
    if(page<1){
      string =`${this.API_ENDPOINT$}mentors/resource-list?mentorId=${mentorId}&page=1`
    }
    if(param){
      string = string +`&search=${param}`
    }
    return this.http.get(string);
  }

  unassignedResources(page:any,limit:any,param?:any): Observable<any> {

    let string =`${this.API_ENDPOINT$}mentors/resource-list?&page=${page}`
    if(page<1){
      string =`${this.API_ENDPOINT$}mentors/resource-list?&page=1`
    }
    if(param){
      string = string +`&search=${param}`
    }
    return this.http.get(string);
  }

  allocateResources(allocationMode:any, mentorId:any, users:any): Observable<any> {
      return this.http.post(this.API_ENDPOINT$+'mentors/allocation-control?&allocationMode='+allocationMode+'&mentorId='+mentorId,users);
    }


  deallocateResources(allocationMode:any, mentorId:any, users:any): Observable<any> {

    return this.http.post(this.API_ENDPOINT$+'mentors/allocation-control?&allocationMode='+allocationMode+'&mentorId='+mentorId,users);

  }

}
