import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ForgotpasswordComponent } from './core/components/forgotpassword/forgotpassword.component';
import { LoginComponent } from './core/components/login/login.component';
import { ResetpasswordComponent } from './core/components/resetpassword/resetpassword.component';
import { AuthGuard } from './core/guards/auth.guard';
import { LoginguardGuard } from './core/guards/loginguard.guard';
import { ProfileComponent } from './shared/components/profile/profile.component';
import { ChangePasswordComponent } from './core/components/change-password/change-password.component';
import { ManagerResourceAllocationComponent } from './feature/allocation/manager-resource-allocation/manager-resource-allocation.component';

import { GroupAllocationComponent } from './feature/allocation/group-allocation/group-allocation.component';
import { IndexComponent } from './feature/index/index.component';
import { ManagerCreateComponent } from './feature/Manager_Management/manager-create/manager-create.component';
import { ManagerUpdateComponent } from './feature/Manager_Management/manager-update/manager-update.component';
import { AllocateResourceComponent } from './feature/Manager_Management/allocate-resource/allocate-resource.component';
import { ManagerListComponent } from './feature/Manager_Management/manager-list/manager-list.component';
import { MentorListComponent } from './feature/Mentor_Management/mentor-list/mentor-list.component';
import { TrainerListComponent } from './feature/Trainer_Management/trainer-list/trainer-list.component';
import { TraineeListComponent } from './feature/Trainee_Management/trainee-list/trainee-list.component';
import { BatchListComponent } from './feature/Batch_Management/batch-list/batch-list.component';

import { JoinerBatchComponent } from './feature/Batch_Management/joiner-batch/joiner-batch.component';
import { JoinerGroupComponent } from './feature/Group_Management/joiner-group/joiner-group.component';
import { GroupListComponent } from './feature/Group_Management/group-list/group-list.component';
import { TrainingListComponent } from './feature/Training_Management/training-list/training-list.component';
import { DashboardComponent } from './shared/components/dashboard/dashboard.component';
import { MentorResourceAllocationComponent } from './feature/Mentor_Management/mentor-resource-allocation/mentor-resource-allocation.component';
import { ResourceAllocationComponent } from './feature/Mentor_Management/resource-allocation/resource-allocation.component';
import { GroupResourceAllocationComponent } from './feature/Group_Management/group-resource-allocation/group-resource-allocation.component';
import { SessionListComponent } from './feature/Session_Management/session-list/session-list.component';
import { TaskListComponent } from './feature/Task_Management/task-list/task-list.component';
import { TraineeSessionListComponent } from './feature/Session_Management/trainee-session-list/trainee-session-list.component';
import { TraineeTaskListComponent } from './feature/Task_Management/trainee-task-list/trainee-task-list.component';
import { ManageTaskComponent } from './feature/Task_Management/manage-task/manage-task.component';
import { SubtaskManagementComponent } from './feature/Subtask_Management/subtask-management/subtask-management.component';


const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  {
        path: 'login',
        component: LoginComponent,
        title: 'Login',
        canActivate: [LoginguardGuard],
      },
      {
        path: 'forgot_password',
        component: ForgotpasswordComponent,
        title: 'Forgot Password',
      },
      {
        path: 'reset_password/:token',
        component: ResetpasswordComponent,
        title: 'Reset Password',
      },
    {
        path: '', component: IndexComponent, children:[

          {
            path: 'dashboard', component: DashboardComponent, title: 'Dashboard', data: { breadcrumb: 'Home' } ,canActivate: [AuthGuard]
          },
          {
            path: 'profile', component: ProfileComponent, title: 'Profile', data: { breadcrumb: 'Profile' }, canActivate: [AuthGuard]
          },
          {
            path: 'change_password', component: ChangePasswordComponent,title: 'Change Password', data: { breadcrumb: 'Change Password' }, canActivate: [AuthGuard]
          },
          {
            path: 'managers', redirectTo: 'managers/list'
          },
          {
            path: 'managers/list', component: ManagerListComponent, title: 'Manager Management', canActivate: [AuthGuard]
          },
          {
            path: 'managers/register', component: ManagerCreateComponent,canActivate: [AuthGuard]
          },
          {
            path: 'managers/update/:id', component: ManagerUpdateComponent,canActivate: [AuthGuard]
          },
          {
            path: 'managers/allocate/:id', component: AllocateResourceComponent,canActivate: [AuthGuard]
          },
          {
            path: 'mentors', redirectTo: 'mentors/list'
          },
          {
            path: 'mentors/list', component: MentorListComponent,title: 'Mentor Management', canActivate: [AuthGuard]
          },
          {
            path: 'mentors/allocate/:id', component: ResourceAllocationComponent,canActivate: [AuthGuard]
          },
          {
            path: 'trainers', redirectTo: 'trainers/list'
          },
          {
            path: 'trainers/list', component: TrainerListComponent, title: 'Trainer Management', canActivate: [AuthGuard]
          },
          {
            path: 'trainees', redirectTo: 'trainees/list'
          },
          {
            path: 'trainees/list', component: TraineeListComponent, title: 'Trainee Management', canActivate: [AuthGuard]
          },
          {
            path: 'batches', redirectTo: 'batches/list'
          },
          {
            path: 'batches/list', component: BatchListComponent,title: 'Batch Management', canActivate: [AuthGuard]
          },
          {
            path: 'groups', redirectTo: 'groups/list'
          },
          {
            path: 'groups/list', component: GroupListComponent, title: 'Group Management', canActivate: [AuthGuard]
          },
          {
            path: 'groups/allocate/:id', component: GroupResourceAllocationComponent,canActivate: [AuthGuard]
          },
          {
            path: 'training', redirectTo: 'training/list'
          },
          {
            path: 'training/list', component: TrainingListComponent,title: 'Training Management', canActivate: [AuthGuard]
          },
          {
            path: 'session', redirectTo: 'session/list'
          },
          {
            path: 'session/list', component: SessionListComponent,title: 'Session Management', canActivate: [AuthGuard]
          },
          {
            path: 'task', redirectTo: 'task/list'
          },
          {
            path: 'task/list', component: TaskListComponent,title: 'Task Management', canActivate: [AuthGuard]
          },
          {
            path: 'task/manage/:id', component: ManageTaskComponent,canActivate: [AuthGuard]
          },
          {
            path: 'trainee_session', redirectTo: 'trainee_session/list'
          },
          {
            path: 'trainee_session/list', component: TraineeSessionListComponent,title: 'My Trainings', canActivate: [AuthGuard]
          },
          {
            path: 'trainee_task', redirectTo: 'trainee_task/list'
          },
          {
            path: 'trainee_task/list', component: TraineeTaskListComponent,title: 'My Tasks', canActivate: [AuthGuard]
          },
          {
            path: 'subtask', redirectTo: 'subtask/list'
          },
          {
            path: 'subtask/list', component: SubtaskManagementComponent,title: 'Subtask Management', canActivate: [AuthGuard]
          },
        ]
      }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
