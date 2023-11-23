import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AppConstants } from 'src/app/app.constants';

@Injectable({
  providedIn: 'root'
})
export class SubtaskService {

  page:any;
  limit:any;
  taskMode:any;

  constructor(private http: HttpClient) { }

  private API_ENDPOINT$ = AppConstants.API_ENDPOINT;

  getSubtask(page:any,limit:any,taskMode:any,param?:any): Observable<any> {
    let string =`${this.API_ENDPOINT$}task?limit=${limit}&page=${page}&taskMode=${taskMode}`
    if(page<1){
      string =`${this.API_ENDPOINT$}task?&page=1`
    }
    if(param){
      if(param.trainingId){
        string=string+`&trainingId=${param.trainingId}`
      }
      if(param.search){
        string=string+`&search=${param.search}`
      }
      if(param.taskId){
      string=string+`&taskId=${param.taskId}`
      }
    }
    console.log(param,"param");
    
    return this.http.get(string);
  }

  getTaskList(): Observable<any> {
    return this.http.get(this.API_ENDPOINT$+'task/taskList');
  }

  addSubtask(data: any): Observable<any> {    
    return this.http.post(this.API_ENDPOINT$+'subTask',data);
  }

  DetailTaskView(taskId: any): Observable<any> {
    return this.http.get(this.API_ENDPOINT$+'task/'+taskId);
  }

  deleteTask(taskId:any): Observable<any> {
    return this.http.delete(this.API_ENDPOINT$+'task/'+taskId);
  }

  getTrainingList(): Observable<any> {
    return this.http.get(this.API_ENDPOINT$+'trainingSession/trainingList');
  }

  // getTaskList(): Observable<any> {
  //   return this.http.get(this.API_ENDPOINT$+'task/taskList');
  // }
}
