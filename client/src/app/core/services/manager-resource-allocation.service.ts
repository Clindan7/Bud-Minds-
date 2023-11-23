import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { AppConstants } from 'src/app/app.constants';

@Injectable({
  providedIn: 'root'
})
export class ManagerResourceAllocationService {

  page:any;
  limit:any;
  constructor(private http: HttpClient, private route: ActivatedRoute) { }
  private API_ENDPOINT$ = AppConstants.API_ENDPOINT;

  assignedResources(page:any,limit:any,managerId:any, userRole:any,param?:any): Observable<any> {
    let string =`${this.API_ENDPOINT$}managers/resource-list?&managerId=${managerId}&userRole=${userRole}&page=${page}`
    if(page<1){
      string =`${this.API_ENDPOINT$}managers/resource-list?&managerId=${managerId}&userRole=${userRole}&page=1`
    }
    if(param){
      string = string +`&search=${param}`
    }
    return this.http.get(string);
  }

 
totalResources(page:any,limit:any,userRole:any,param?:any): Observable<any> {
    let string =`${this.API_ENDPOINT$}managers/resource-list?userRole=${userRole}&page=${page}`
    if(page<1){
      string =`${this.API_ENDPOINT$}managers/resource-list?&userRole=${userRole}&page=1`
    }
    if(param){
      string = string +`&search=${param}`
    }
    return this.http.get(string);
  }



  allocateResources(allocationMode:any, managerId:any, userRole:any, users:any): Observable<any> {
      return this.http.post(this.API_ENDPOINT$+'managers/allocation-control?&allocationMode='+allocationMode+'&managerId='+managerId+'&userRole='+userRole,users);
    }


  deallocateResources(allocationMode:any, managerId:any, userRole:any, users:any): Observable<any> {

    return this.http.post(this.API_ENDPOINT$+'managers/allocation-control?&allocationMode='+allocationMode+'&managerId='+managerId+'&userRole='+userRole,users);

  }

}

