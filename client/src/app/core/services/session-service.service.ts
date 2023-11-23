import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AppConstants } from 'src/app/app.constants';

@Injectable({
  providedIn: 'root'
})
export class SessionServiceService {

  page:any;
  limit:any;

  constructor(private http: HttpClient) { }

  private API_ENDPOINT$ = AppConstants.API_ENDPOINT;

  fetchUser(): Observable<any> {
    return this.http.get(this.API_ENDPOINT$+'users/profile');
  }

  getSession(page:any,limit:any,param?:any): Observable<any> {
    let string =`${this.API_ENDPOINT$}trainingSession?limit=${limit}&page=${page}`
    if(page<1){
      string =`${this.API_ENDPOINT$}trainingSession?&page=1`
    }
    if(param){
      if(param.trainingId){
        string=string+`&trainingId=${param.trainingId}`
      }

      if(param.technologyId){
        string=string+`&technologyId=${param.technologyId}`
      }

      if(param.trainerId){
      string=string+`&trainerId=${param.trainerId}`
      }

      if(param.groupId){
      string = string +`&joinerGroupId=${param.groupId}`
      }
    }
    return this.http.get(string);
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

  getTrainer(page:any,limit:any,param?:any): Observable<any> {
    let string =`${this.API_ENDPOINT$}trainers?&page=${page}&limit=${limit}&status=1`
    if(page<1){
      string =`${this.API_ENDPOINT$}trainers?&page=1`
    }
    // if(param){
    //   if(param.employeeId)
    //   string=string+`&employeeId=${param.employeeId}`

    //   if(param.name)
    //   string = string +`&search=${param.name}`
    // }
    return this.http.get(string);
  }
  

  getTrainingList(): Observable<any> {
    return this.http.get(this.API_ENDPOINT$+'trainingSession/trainingList');
  }

  getTechnologyList(): Observable<any> {
    return this.http.get(this.API_ENDPOINT$+'trainingSession/technologyList');
  }

  getGroupList(): Observable<any> {
    return this.http.get(this.API_ENDPOINT$+'trainingSession/groupList');
  }

  getTrainerList(): Observable<any> {
    return this.http.get(this.API_ENDPOINT$+'trainingSession/trainerList');
  }

  addSession(data: any): Observable<any> {
    return this.http.post(this.API_ENDPOINT$+'trainingSession',data);
  }

  deleteSession(sessionId:any): Observable<any> {
    return this.http.delete(this.API_ENDPOINT$+'trainingSession/'+sessionId);
  }

  changePassword(data: any): Observable<any> {
    return this.http.put(this.API_ENDPOINT$+'users/changePassword',data);
  }

  addTraining(data: any): Observable<any> {
    return this.http.post(this.API_ENDPOINT$+'training/',data);
  }

  editSession(data:any,sessionId: any): Observable<any> {
    console.log(data);
    console.log(sessionId);
    
    return this.http.put(this.API_ENDPOINT$+`trainingSession?&sessionId=${sessionId}`, data);
  }

  viewSession(sessionId: any): Observable<any> {
    return this.http.get(this.API_ENDPOINT$+'trainingSession/'+sessionId);
  }

  setPageNum(pageNum:any){
    this.page=pageNum;
  }

  getUpcomingTraining(): Observable<any> {
    return this.http.get(this.API_ENDPOINT$+'training/upcomingTrainings');
  }




  // getTechnology(): Observable<any> {
  //   return this.http.get(this.API_ENDPOINT$+'technology');
  // }

  // addTechnology(data: any): Observable<any> {
  //   return this.http.post(this.API_ENDPOINT$+'technology/',data);
  // }

  // deleteTechnology(technologyId:any): Observable<any> {
  //   return this.http.delete(this.API_ENDPOINT$+'technology/'+technologyId);
  // }

  // deleteTraining(trainingId:any): Observable<any> {
  //   return this.http.delete(this.API_ENDPOINT$+'training/'+trainingId);
  // }

 



  // editTraining(data:any,trainingId: any): Observable<any> {
  //   return this.http.put(this.API_ENDPOINT$+'training/'+trainingId, data);
  // }

  viewTraining(trainingId: any): Observable<any> {
    return this.http.get(this.API_ENDPOINT$+'training/'+trainingId);
  }

 


}

