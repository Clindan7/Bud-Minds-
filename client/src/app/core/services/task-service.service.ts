import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AppConstants } from 'src/app/app.constants';

@Injectable({
  providedIn: 'root'
})
export class TaskServiceService {

  page:any;
  limit:any;
  taskMode:any;

  constructor(private http: HttpClient) { }

  private API_ENDPOINT$ = AppConstants.API_ENDPOINT;

  getTask(page:any,limit:any,taskMode:any,param?:any): Observable<any> {
    let string =`${this.API_ENDPOINT$}task?limit=${limit}&page=${page}&taskMode=${taskMode}`
    if(page<1){
      string =`${this.API_ENDPOINT$}task?&page=1`
    }
    if(param){
      if(param.trainingId){
        string=string+`&trainingId=${param.trainingId}`
      }
      if(param.technologyId){
        string=string+`&technologyId=${param.technologyId}`
      }
      if(param.taskId){
      string=string+`&taskId=${param.taskId}`
      }
    }
    return this.http.get(string);
  }

  getTraineeTask(page:any,limit:any,param?:any): Observable<any> {
    
    let string =`${this.API_ENDPOINT$}task/traineeTask-list?limit=${limit}&page=${page}`
    if(page<1){
      string =`${this.API_ENDPOINT$}task/traineeTask-list?&page=1`
    }
    if(param){
      if(param.groupId){
        string=string+`&joinerGroupId=${param.groupId}`
      }
    }
    return this.http.get(string);
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

  // task/userGroupList/{taskId}

  assignedGroups(page:any,limit:any,taskId:any,param?:any): Observable<any> {
    if (page == null){
      page = 1;
    }

    let string =`${this.API_ENDPOINT$}task/userGroupList/${taskId}`
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

  deleteTask(taskId:any): Observable<any> {
    return this.http.delete(this.API_ENDPOINT$+'task/'+taskId);
  }

  addTask(data: any): Observable<any> {    
    return this.http.post(this.API_ENDPOINT$+'task/add',data);
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

  viewTraining(trainingId: any): Observable<any> {
    return this.http.get(this.API_ENDPOINT$+'training/'+trainingId);
  }

  getTrainingList(): Observable<any> {
    return this.http.get(this.API_ENDPOINT$+'trainingSession/trainingList');
  }

  getTaskList(): Observable<any> {
    return this.http.get(this.API_ENDPOINT$+'task/taskList');
  }

  getTechnologyList(): Observable<any> {
    return this.http.get(this.API_ENDPOINT$+'trainingSession/technologyList');
  }

  DetailTaskView(taskId: any): Observable<any> {
    return this.http.get(this.API_ENDPOINT$+'task/'+taskId);
  }

  editTask(data:any,taskId: any): Observable<any> {
    console.log(data);
    console.log(taskId);
    return this.http.put(this.API_ENDPOINT$+'task/'+taskId, data);
  }
  getUpcomingTraining(): Observable<any> {
    return this.http.get(this.API_ENDPOINT$+'training/upcomingTrainings');
  }
}
// return this.http.delete(this.API_ENDPOINT$+'task/'+taskId);
