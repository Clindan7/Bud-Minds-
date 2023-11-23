import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ManagerResourceAllocationService } from 'src/app/core/services/manager-resource-allocation.service';
import { SessionServiceService } from 'src/app/core/services/session-service.service';
import { TaskServiceService } from 'src/app/core/services/task-service.service';
import { TraineeValuationComponent } from '../../Valuation_Management/trainee-valuation/trainee-valuation.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-manage-task',
  templateUrl: './manage-task.component.html',
  styleUrls: ['./manage-task.component.css']
})
export class ManageTaskComponent implements OnInit {

  assigned:any;
  unassigned:any;
  taskId: any;
  allocate: any;
  deallocate: any;
  page: any = 1;
  page2: any =1;
  limit: any = 10;
  newArray: Array<number>[] = [];
  checked: any = 0;
  userRole: any = 2;
  len: any;
  lenn:any;
  leng:any;
  length:any;
  users:any;
  mode:any=2;
  i:any;
  masterSelected:boolean
  selectallAssigned: boolean =false;
  selectAllElements: boolean =false;
  selectallUnassigned:boolean = false;
  selectElement: boolean =false;
  submitted: boolean =false;
  res:any;
  rest:any;
  result:any;
  response:any;
  blnChangePage:boolean=false;
  searchTaskName!: FormGroup;
  traineeData: any;
  skeletonData = Array(10).fill({});
  groups:any;

  constructor(private fb:FormBuilder,
    private sessionService: SessionServiceService,
    private managerResource: ManagerResourceAllocationService,
    private taskService: TaskServiceService,
    private modalService:NgbModal,
    private route: ActivatedRoute,
    private toast: ToastrService
    ) { 
      this.masterSelected = false;
    }

  searchUser: FormGroup = this.fb.group({
    searchAssignedGroups: '',
    searchUnassignedGroups:''
  });

  searchGroupName: FormGroup = this.fb.group({
    groupId: new FormControl('')
  });

  ngOnInit(): void {
    this.taskId = this.route.snapshot.params['id'];
    this.assignedGroups(this.page,this.limit,this.taskId, this.userRole);
    this.totalGroups(this.page,this.limit);
    this.searchTrainee(this.page, this.limit);
    this.getGroup();
  }

  getGroup() {
    this.sessionService.getGroups(1,20000).subscribe({
      next: (resp: any) => {
        this.groups = resp.result;
      },
      error: (err: any) => {
        if (err.error.errorCode == '1960') {
          this.toast.error('Group not found', '');
        } else if (err.error.errorCode == "1908") {
          this.toast.error('Invalid token', '');
        } else if (err.error.errorCode == '1909') {
          this.toast.error('Authorization token expired', '');
        }
      },
    });
  }

  assignedGroups(page: any, limit: any,managerId:any,searchGroup?:any) {
    this.newArray=[];
    this.selectallAssigned=false;
    this.selectAllElements=false;
    if(this.searchUser.value.searchAssignedGroups!=''){
      page=1;
  }
    this.taskService
      .assignedGroups(page,limit,this.taskId,this.searchUser.value.searchAssignedGroups)
      .subscribe({
        next: (resp: any) => {
          this.submitted = true;
          this.res=resp;
          this.len = resp.numItems;
          this.assigned = resp.result;
        },
        error: (err: any) => {
          if (err.error.errorCode == '1093') {
            this.toast.error(
              'User has no permission to perform this action',
              ''
            );
          } else if (err.error.errorCode == '1912') {
            this.toast.error('User not found', '');
          } else if (err.error.errorCode == '1996') {
            this.toast.error('Manager not found', '');
          } else if (err.error.errorCode == '1995') {
            this.toast.error('Invalid user role', '');
          } 
        },
         complete: () => {
          this.submitted = false;
          this.mode=2;
        }
      });
  }

  totalGroups(page: any, limit: any, searchMgr?: any) {
    this.newArray=[];
    this.selectallUnassigned=false;
    this.selectElement=false;
    if(this.searchUser.value.searchUnassignedGroups!=''){
      page=1;
  }
    this.taskService.getGroups(page,limit,this.searchUser.value.searchUnassignedGroups).subscribe({
      next: (resp: any) => {
        this.submitted = true;
        this.rest = resp;
        this.lenn = resp.numItems;
        this.unassigned = resp.result;
      },
      error: (err: any) => {
        this.submitted=false;
        if (err.error.errorCode == '1093') {
          this.toast.error('User has no permission to perform this action', '');
        } else if (err.error.errorCode == '1912') {
          this.toast.error('User not found', '');
        } else if (err.error.errorCode == '1996') {
          this.toast.error('Manager not found', '');
        } else if (err.error.errorCode == '1995') {
          this.toast.error('Invalid user role', '');
        }
      },
       complete: () => {
          this.submitted = false;
          this.mode=2;
        }
    });
  }

  allocateGroups() {
    
    let param = {
      users: this.newArray,
    };
    this.submitted=true;
    this.managerResource
      .allocateResources(1, this.taskId, this.userRole, param)
      .subscribe({
        next: (resp: any) => {
          this.toast.success('Group sucessfully allocated to Task', '');
          this.newArray.length = 0;
        },
        error: (err: any) => {
          this.submitted=false;
          if (err.error.errorCode == '1093') {
            this.toast.error(
              'User has no permission to perform this action',
              ''
            );
          } else if (err.error.errorCode == '1940') {
            this.toast.error(
              'The manager user is either deallocated already or not found',
              ''
            );
          } else if (err.error.errorCode == '1912') {
            this.toast.error('User not found', '');
          } else if (
            err.error.errorCode == '1931' &&
            err.error.errorMessage == 'Users should be of type java.util.List'
          ) {
            this.toast.error('Users should be of type list', '');
          } else if (
            err.error.errorCode == '1931' &&
            err.error.errorMessage == 'UserRole should be of type byte'
          ) {
            this.toast.error('UserRole should be type byte', '');
          } else if (
            err.error.errorCode == '1931' &&
            err.error.errorMessage ==
              'managerId should be of type type java.util.Integer'
          ) {
            this.toast.error('managerId should be of type Integer', '');
          } else if (
            err.error.errorCode == '1931' &&
            err.error.errorMessage == 'AllocationMode should be of type byte'
          ) {
            this.toast.error('AllocationMode should be of type byte', '');
          } else if (err.error.errorCode == '1994') {
            this.toast.error('Users not selected', '');
          } else if (err.error.errorCode == '1941') {
            this.toast.error('Invalid allocation mode', '');
          } else if (err.error.errorCode == '1942') {
            this.toast.error('Manager ID is required', '');
          } else if (err.error.errorCode == '1995') {
            this.toast.error('Invalid user role', '');
          } else if (err.error.errorCode == '1996') {
            this.toast.error('Manager not found', '');
          }
        },
        complete: () => {          
          // this.selectallUnassigned=false;
          // this.selectElement=false;
          // this.submitted=false;
          // this.mode=null;
          // if(this.rest.hasNext==true){
          // this.assignedMentors(this.page,this.limit,this.managerId);
          // this.totalMentors(this.page2,this.limit);
          // }
          // else{
          //   this.totalMentors(this.page2-1,this.limit)
          // this.assignedMentors(this.page-1,this.limit,this.managerId);
          // }
        },
      }).add(()=>
      {
          this.selectallUnassigned=false;
          this.selectElement=false;
          this.submitted=false;
          this.mode=null;
          if(this.rest.hasNext==true){
          this.assignedGroups(this.page,this.limit,this.taskId);
          this.totalGroups(this.page2,this.limit);
          }
          else{
            this.totalGroups(this.page2-1,this.limit)
          this.assignedGroups(this.page-1,this.limit,this.taskId);
          }
      });
  }

  deallocateGroups() {
    let param = {
      users: this.newArray,
    };
    this.submitted=true;
    this.managerResource
      .deallocateResources(0, this.taskId, this.userRole, param)
      .subscribe({
        next: (resp: any) => { 
          this.toast.success('Group sucessfully deallocated from task', '');
          this.newArray.length = 0;
        },
        error: (err: any) => {
          this.submitted=false;
          if (err.error.errorCode == '1093') {
            this.toast.error(
              'User has no permission to perform this action',
              ''
            );
          } else if (err.error.errorCode == '1940') {
            this.toast.error(
              'The manager user is either deallocated already or not found',
              ''
            );
          } else if (err.error.errorCode == '1912') {
            this.toast.error('User not found', '');
          } else if (
            err.error.errorCode == '1931' &&
            err.error.errorMessage == 'Users should be of type java.util.List'
          ) {
            this.toast.error('Users should be of type list', '');
          } else if (
            err.error.errorCode == '1931' &&
            err.error.errorMessage == 'UserRole should be of type byte'
          ) {
            this.toast.error('UserRole should be type byte', '');
          } else if (
            err.error.errorCode == '1931' &&
            err.error.errorMessage ==
              'managerId should be of type type java.util.Integer'
          ) {
            this.toast.error('managerId should be of type Integer', '');
          } else if (
            err.error.errorCode == '1931' &&
            err.error.errorMessage == 'AllocationMode should be of type byte'
          ) {
            this.toast.error('AllocationMode should be of type byte', '');
          } else if (err.error.errorCode == '1994') {
            this.toast.error('Users not selected', '');
          } else if (err.error.errorCode == '1941') {
            this.toast.error('Invalid allocation mode', '');
          } else if (err.error.errorCode == '1942') {
            this.toast.error('Manager ID is required', '');
          } else if (err.error.errorCode == '1995') {
            this.toast.error('Invalid user role', '');
          } else if (err.error.errorCode == '1996') {
            this.toast.error('Manager not found', '');
          } 
        },
        complete: () => {
          console.log("complee");
          
        //   this.selectAllElements=false;
        //   this.selectallAssigned=false;
        //   this.submitted=false;
        // if(this.res.hasNext==true)
        //   {
        //   this.assignedMentors(this.page,this.limit,this.managerId);
        //   this.totalMentors(this.page2,this.limit);}
        //   else{
        //   this.totalMentors(this.page2-1,this.limit)
        //   this.assignedMentors(this.page-1,this.limit,this.managerId);
        //   }
        //   this.mode=null;
        },
      }).add(()=>
      {
        console.log("add");
        
        this.selectAllElements=false;
          this.selectallAssigned=false;
          this.submitted=false;
        if(this.res.hasNext==true)
          {
            console.log("first");
            
          this.assignedGroups(this.page,this.limit,this.taskId);
          this.totalGroups(this.page2,this.limit);
        }
        else{
          console.log("sec");
          
          this.totalGroups(this.page2-1,this.limit)
          this.assignedGroups(this.page-1,this.limit,this.taskId);
          }
          this.mode=null;
      });
  }

  searchTrainee(page: any, limit: any, searchTask?: any) {
    
    
    if((this.searchGroupName.value.groupId!="") && !this.blnChangePage){      
      this.page=1
      page=1
      this.blnChangePage=false
    }

    this.taskService
      .assignedGroups(page, limit,this.taskId,this.searchGroupName.value)
      .subscribe({
        next:(res: any) => {
          this.len = res.numItems;
          this.traineeData = res; 
          // this.toggleSkeleton(false);
          this.blnChangePage=false;
        },
        error: (err: any) => {
          console.log(err, 'error');
          if (err.error.errorCode == "1913"){
            this.toast.error('Task not found', '');
          } else if (err.error.errorCode == "1908") {
            this.toast.error('Invalid token', '');
          } else if (err.error.errorCode == '1909') {
            this.toast.error('Authorization token expired', '');
          }
          this.traineeData=[];
        },
      });
  }

  onCheckboxChange(e: any, data: any, mode: any) {   
    if (e.target.checked) {          
      this.newArray.push(data);
    } else {
      let removeIndex = this.newArray.findIndex((itm) => itm === data);
      if (removeIndex !== -1) 
      this.newArray.splice(removeIndex, 1);
    }
    if(mode==0){
      this.mode=0;
    }
    if(mode==1){
      this.mode=1;
    }
    if(this.newArray.length==0){
      this.mode=2;
    }
  }
  onPageChange($event,list:number) {   
    window.scrollTo({
      top: 0,
      behavior: 'smooth'
    });
    switch(list){
      case 1:
        this.page=$event;
        break;
      case 2:
        this.page2=$event;
        break;
      
    }
       
    this.assignedGroups(this.page, this.limit,this.taskId);
    this.totalGroups(this.page2,this.limit);
  }
  
  clearAssignedGroupsSearchTerm(): void {
    this.searchUser.controls['searchAssignedGroups'].reset();
    this.searchUser.value.searchAssignedGroups="";
  }

  clearUnassignedGroupsSearchTerm(): void {
    this.searchUser.controls['searchUnassignedGroups'].reset();
    this.searchUser.value.searchUnassignedGroups="";
  }

  selectAll(e:any,data:any){
    this.mode=0
    this.newArray=[];
    
    if(e.target.checked){
      this.selectallAssigned=true;
      this.selectAllElements=true;
      
      for(let i=0;i<data.length;i++){
      
      
      this.newArray.push(data[i].userId)
      }
    }
    else{
    this.selectallAssigned=false;
    this.selectAllElements=false;
    for(let i=0;i<data.length;i++){
      this.newArray.pop()
      }
      this.mode=null;
    }
  }

  selectAllUnassigned(e:any,data:any){
    this.mode=1
    this.newArray=[];
    
    if(e.target.checked){
      this.selectElement=true;
      this.selectallUnassigned=true;
      
      for(let i=0;i<data.length;i++){  
      this.newArray.push(data[i].userId)
      }
    }
    else{
    this.selectElement=false;
    this.selectallUnassigned=false;
    for(let i=0;i<data.length;i++){
      this.newArray.pop()
      }
      this.mode=null;
    }
  }

  checkUncheckAll(e: any, data: any) {
  
    if (e.target.checked) {
      for(let i=0;i<data.length;i++)
      this.newArray.push(data[i].userId);
    }
    else {
      let removeIndex = this.newArray.findIndex((itm) => itm === data);
      if (removeIndex !== -1) 
      this.newArray.splice(removeIndex, 1);
    }
    if (this.assigned.every(val => val.assigned == true))
      this.assigned.forEach(val => { val.assigned = false });
    else
      this.assigned.forEach(val => { val.assigned = true });
  }

  onPageChangee($event) {
    this.blnChangePage=true;
    window.scrollTo({
      top: 0,
      behavior: 'smooth'
    });

    this.page = $event;
    this.searchTrainee(this.page,this.limit,this.searchGroupName.value);
  }

  score(){
      let modalRef = this.modalService.open(TraineeValuationComponent, {
        centered: true,
        backdrop:'static'
      });
      modalRef.result.then(
        (result) => {
          this.searchTrainee(this.page,this.limit);
        },
        () => { });
    }
  

}

