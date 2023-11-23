import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ManagerResourceAllocationService } from 'src/app/core/services/manager-resource-allocation.service';

@Component({
  selector: 'app-allocate-resources',
  templateUrl: './allocate-resources.component.html',
  styleUrls: ['./allocate-resources.component.css']
})
export class AllocateResourcesComponent implements OnInit {

  assigned:any;
  unassigned:any;
  managerId: any;
  allocate: any;
  deallocate: any;
  page: any = 1;
  page2: any =1;
  page3: any = 1;
  page4: any = 1;
  limit: any = 10;
  newArray: Array<number>[] = [];
  checked: any = 0;
  userRole: any = 2;
  userRoleTrainee: any = 4;
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

  constructor(private fb:FormBuilder,
    private managerResource: ManagerResourceAllocationService,
    private route: ActivatedRoute,
    private toast: ToastrService
    ) { 
      this.masterSelected = false;
    }

  searchUser: FormGroup = this.fb.group({
    searchAssignedMentors: '',
    searchUnassignedMentors:'',
    searchAssignedTrainees: '',
    searchUnassignedTrainees:''
  });

  ngOnInit(): void {
    this.managerId = this.route.snapshot.params['id'];
    this.assignedMentors(this.page,this.limit,this.managerId, this.userRole);
    this.assignedTrainees(this.page,this.limit,this.managerId, this.userRoleTrainee);
    this.totalMentors(this.page,this.limit);
    this.totalTrainees(this.page,this.limit);
  }

  assignedMentors(page: any, limit: any,managerId:any,searchMentor?:any) {
    this.newArray=[];
    this.selectallAssigned=false;
    this.selectAllElements=false;
    if(this.searchUser.value.searchAssignedMentors!=''){
      page=1;
  }
    this.managerResource
      .assignedResources(page,limit,this.managerId, this.userRole,this.searchUser.value.searchAssignedMentors)
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

  assignedTrainees(page: any, limit: any,managerId:any,searchTrainee?:any) {
    this.newArray=[];
    this.selectallAssigned=false;
    this.selectAllElements=false;
    if(this.searchUser.value.searchAssignedTrainees!=''){
      page=1;
  }
    this.managerResource
    .assignedResources(page,limit,this.managerId, this.userRoleTrainee, this.searchUser.value.searchAssignedTrainees)
      .subscribe({
        next: (resp: any) => {
          this.submitted =true;    
          this.result = resp;      
          this.leng = resp.numItems;
          this.deallocate = resp.result;
        },
        error: (err: any) => {
          if (err.error.errorCode == '1093') {
            this.toast.error(
              'User has no permission to access this action',
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

  totalMentors(page: any, limit: any, searchMgr?: any) {
    this.newArray=[];
    this.selectallUnassigned=false;
    this.selectElement=false;
    if(this.searchUser.value.searchUnassignedMentors!=''){
      page=1;
  }
    this.managerResource.totalResources(page,limit,2,this.searchUser.value.searchUnassignedMentors).subscribe({
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

  totalTrainees(page: any, limit: any, searchMgr?: any) {
    this.newArray=[];
    this.selectallUnassigned=false;
    this.selectElement=false;
    if(this.searchUser.value.searchUnassignedTrainees!=''){
      page=1;
  }
    this.managerResource.totalResources(page, limit,4,this.searchUser.value.searchUnassignedTrainees).subscribe({
      next: (resp: any) => {
        this.submitted = true;
        this.response = resp;
        this.length = resp.numItems;
        this.allocate = resp.result;
      },
      error: (err: any) => {
        this.submitted = false;
        if (err.error.errorCode == '1093') {
          this.toast.error('User has no permission to access this action', '');
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

  allocateMentors() {
    
    let param = {
      users: this.newArray,
    };
    this.submitted=true;
    this.managerResource
      .allocateResources(1, this.managerId, this.userRole, param)
      .subscribe({
        next: (resp: any) => {
          this.toast.success('Mentor sucessfully allocated to manager', '');
          this.newArray.length = 0;
          // this.totalMentors(this.page,this.limit);
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
          this.assignedMentors(this.page,this.limit,this.managerId);
          this.totalMentors(this.page2,this.limit);
          }
          else{
            this.totalMentors(this.page2-1,this.limit)
          this.assignedMentors(this.page-1,this.limit,this.managerId);
          }
      });
  }

  deallocateMentors() {
    let param = {
      users: this.newArray,
    };
    this.submitted=true;
    this.managerResource
      .deallocateResources(0, this.managerId, this.userRole, param)
      .subscribe({
        next: (resp: any) => { 
          this.toast.success('Mentor sucessfully deallocated from manager', '');
          this.newArray.length = 0;
          // this.assignedMentors(this.page,this.limit,this.managerId);
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
            
          this.assignedMentors(this.page,this.limit,this.managerId);
          this.totalMentors(this.page2,this.limit);
        }
        else{
          console.log("sec");
          
          this.totalMentors(this.page2-1,this.limit)
          this.assignedMentors(this.page-1,this.limit,this.managerId);
          }
          this.mode=null;
      });
  }

  allocateTrainees() {
    let param = {
      users: this.newArray,
    };
    this.submitted=true;
    this.managerResource
      .allocateResources(1, this.managerId, this.userRoleTrainee, param)
      .subscribe({
        next: (resp: any) => {
          this.toast.success('Trainees sucessfully allocated to manager', '');
          this.newArray.length = 0;
          // this.totalTrainees(this.page,this.limit);
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
          } else if (err.error.errorCode == '1908') {
            this.toast.error('Invalid token', '');
          } else if (err.error.errorCode == '1909') {
            this.toast.error('Authorization token expired', '');
          }
        },
        complete: () => {
          this.selectallUnassigned=false;
          this.selectElement=false;
          this.submitted = false;
          if(this.response.hasNext==true){
            this.assignedTrainees(this.page3,this.limit,this.managerId);
            this.totalTrainees(this.page4,this.limit);
            }
            else{
              this.totalTrainees(this.page4-1,this.limit)
            this.assignedTrainees(this.page3-1,this.limit,this.managerId);
            }
          this.mode=null;
        },
      }).add(()=>
      {
        // this.selectallUnassigned=false;
        // this.selectElement=false;
        // this.submitted = false;
        // if(this.response.hasNext==true){
        //   this.assignedTrainees(this.page3,this.limit,this.managerId);
        //   this.totalTrainees(this.page4,this.limit);
        //   }
        //   else{
        //     this.totalTrainees(this.page4-1,this.limit)
        //   this.assignedTrainees(this.page3-1,this.limit,this.managerId);
        //   }
        // this.mode=null;
      });
  }

  deallocateTrainees() {

    let param = {
      users: this.newArray,
    };
    this.submitted=true;
    this.managerResource
      .deallocateResources(0, this.managerId, this.userRoleTrainee, param)
      .subscribe({
        next: (resp: any) => {
          this.toast.success(
            'Trainees sucessfully deallocated from manager',
            ''
          );
          this.newArray.length = 0;
          // this.assignedTrainees(this.page,this.limit,this.managerId);
        },
        error: (err: any) => {
          this.submitted = false;
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
          } else if (err.error.errorCode == '1908') {
            this.toast.error('Invalid token', '');
          } else if (err.error.errorCode == '1909') {
            this.toast.error('Authorization token expired', '');
          }
        },
        complete: () => {
          this.selectAllElements=false;
          this.selectallAssigned=false;
          this.submitted = false;
          if(this.result.hasNext==true)
          {
          this.assignedTrainees(this.page3,this.limit,this.managerId);
          this.totalTrainees(this.page4,this.limit);}
          else{
          this.totalTrainees(this.page4-1,this.limit)
          this.assignedTrainees(this.page3-1,this.limit,this.managerId);
          }

          this.mode=null;
        },
      }).add(()=>
      {
        // this.selectAllElements=false;
        //   this.selectallAssigned=false;
        //   this.submitted = false;
        //   if(this.result.hasNext==true)
        //   {
        //   this.assignedTrainees(this.page3,this.limit,this.managerId);
        //   this.totalTrainees(this.page4,this.limit);}
        //   else{
        //   this.totalTrainees(this.page4-1,this.limit)
        //   this.assignedTrainees(this.page3-1,this.limit,this.managerId);
        //   }

        //   this.mode=null;
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
      case 3:
        this.page3=$event;
        break;
      case 4:
        this.page4=$event;
        break;
      
    }
       
    this.assignedMentors(this.page, this.limit,this.managerId);
    this.assignedTrainees(this.page3, this.limit,this.managerId);
    this.totalMentors(this.page2,this.limit);
    this.totalTrainees(this.page4,this.limit);
  }
  
  clearAssignedMentorsSearchTerm(): void {
    this.searchUser.controls['searchAssignedMentors'].reset();
    this.searchUser.value.searchAssignedMentors="";
  }

  clearUnassignedMentorsSearchTerm(): void {
    this.searchUser.controls['searchUnassignedMentors'].reset();
    this.searchUser.value.searchUnassignedMentors="";
  }

  clearAssignedTraineesSearchTerm(): void {
    this.searchUser.controls['searchAssignedTrainees'].reset();
    this.searchUser.value.searchAssignedTrainees="";
  }

  clearUnassignedTraineesSearchTerm(): void {
    this.searchUser.controls['searchUnassignedTrainees'].reset();
    this.searchUser.value.searchUnassignedTrainees="";
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

}
