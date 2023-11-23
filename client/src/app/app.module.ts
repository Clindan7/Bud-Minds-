import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbAccordionModule, NgbAlertModule, NgbDatepickerModule, NgbModule} from '@ng-bootstrap/ng-bootstrap';
import { LoginComponent } from './core/components/login/login.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BuddyInterceptorInterceptor } from './core/interceptors/buddy-interceptor.interceptor';
import { ForgotpasswordComponent } from './core/components/forgotpassword/forgotpassword.component';
import { ResetpasswordComponent } from './core/components/resetpassword/resetpassword.component';
import { ProfileComponent } from './shared/components/profile/profile.component';
import { ToastrModule } from 'ngx-toastr';
import { ChangePasswordComponent } from './core/components/change-password/change-password.component';
import { AutocompleteLibModule } from 'angular-ng-autocomplete';
import { NgxPaginationModule } from 'ngx-pagination';
import { CommonModule, DatePipe, JsonPipe } from '@angular/common'
import { ViewModalComponent } from './shared/components/view-modal/view-modal.component';
import {BsDatepickerModule} from 'ngx-bootstrap/datepicker';
import { ManagerResourceAllocationComponent } from './feature/allocation/manager-resource-allocation/manager-resource-allocation.component';
import { MentorToManagerComponent } from './feature/allocation/mentor-to-manager/mentor-to-manager.component';
import { TraineeToManagerComponent } from './feature/allocation/trainee-to-manager/trainee-to-manager.component';
import { TraineeToMentorComponent } from './feature/allocation/trainee-to-mentor/trainee-to-mentor.component';
import { GroupAllocationComponent } from './feature/allocation/group-allocation/group-allocation.component';
import { TraineeToGroupComponent } from './feature/allocation/trainee-to-group/trainee-to-group.component';
import { MentorDelConfirmationComponent } from './shared/modal/mentor-del-confirmation/mentor-del-confirmation.component';
import { AllocateResourceComponent } from './feature/Manager_Management/allocate-resource/allocate-resource.component';
import { ManagerCreateComponent } from './feature/Manager_Management/manager-create/manager-create.component';
import { ManagerUpdateComponent } from './feature/Manager_Management/manager-update/manager-update.component';
import { BreadcrumbComponent } from './shared/layout/breadcrumb/breadcrumb.component';
import { HeaderComponent } from './shared/layout/header/header.component';
import { SidebarComponent } from './shared/layout/sidebar/sidebar.component';
import { UserFormComponent } from './shared/components/user-form/user-form.component';
import { UserListComponent } from './shared/components/user-list/user-list.component';
import { IndexComponent } from './feature/index/index.component';
import { ManagerListComponent } from './feature/Manager_Management/manager-list/manager-list.component';
import { ImportExportComponent } from './shared/components/import-export/import-export.component';
import { DropzoneModule, DropzoneConfigInterface, DROPZONE_CONFIG } from 'ngx-dropzone-wrapper';
import { FiltersComponent } from './feature/filters/filters.component';
import { MentorListComponent } from './feature/Mentor_Management/mentor-list/mentor-list.component';
import { TrainerListComponent } from './feature/Trainer_Management/trainer-list/trainer-list.component';
import { TraineeListComponent } from './feature/Trainee_Management/trainee-list/trainee-list.component';
import { BatchListComponent } from './feature/Batch_Management/batch-list/batch-list.component';
import { JoinerBatchComponent } from './feature/Batch_Management/joiner-batch/joiner-batch.component';
import { JoinerGroupComponent } from './feature/Group_Management/joiner-group/joiner-group.component';
import { GroupListComponent } from './feature/Group_Management/group-list/group-list.component';
import { TrainingListComponent } from './feature/Training_Management/training-list/training-list.component';
import { TrainingFormComponent } from './feature/Training_Management/training-form/training-form.component';
import { TechnologyListComponent } from './feature/Training_Management/technology-list/technology-list.component';
import { TechnologyComponent } from './feature/Training_Management/technology/technology.component';
import { DashboardComponent } from './shared/components/dashboard/dashboard.component';
import { NgxSkeletonLoaderModule } from 'ngx-skeleton-loader';
import { AllocateResourcesComponent } from './shared/components/allocate-resources/allocate-resources.component';
import { ResourceAllocationComponent } from './feature/Mentor_Management/resource-allocation/resource-allocation.component';
import { MentorResourceAllocationComponent } from './feature/Mentor_Management/mentor-resource-allocation/mentor-resource-allocation.component';
import { GroupResourceAllocationComponent } from './feature/Group_Management/group-resource-allocation/group-resource-allocation.component';
import { SessionListComponent } from './feature/Session_Management/session-list/session-list.component';
import { SessionFormComponent } from './feature/Session_Management/session-form/session-form.component';
import { DateTimePickerComponent } from './feature/DateTimePicker/date-time-picker/date-time-picker.component';
import { TimepickerModule } from 'ngx-bootstrap/timepicker';
import { TaskListComponent } from './feature/Task_Management/task-list/task-list.component';
import { TraineeSessionListComponent } from './feature/Session_Management/trainee-session-list/trainee-session-list.component';
import { TaskFormComponent } from './feature/Task_Management/task-form/task-form.component';
import { TraineeTaskListComponent } from './feature/Task_Management/trainee-task-list/trainee-task-list.component';
import { ManageTaskComponent } from './feature/Task_Management/manage-task/manage-task.component';
import { TraineeValuationComponent } from './feature/Valuation_Management/trainee-valuation/trainee-valuation.component';
import { SubtaskManagementComponent } from './feature/Subtask_Management/subtask-management/subtask-management.component';
import { SubtaskFormComponent } from './feature/Subtask_Management/subtask-form/subtask-form.component';
import { TraineeSubtaskListComponent } from './feature/Subtask_Management/trainee-subtask-list/trainee-subtask-list.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    ForgotpasswordComponent,
    ResetpasswordComponent,
    ProfileComponent,
    ChangePasswordComponent,
    ManagerListComponent,
    GroupListComponent,
    BatchListComponent,
    JoinerGroupComponent,
    ViewModalComponent,
    JoinerBatchComponent,
    MentorListComponent,
    TraineeListComponent,
    TrainerListComponent,
    TrainingListComponent,
    ManagerResourceAllocationComponent,
    TechnologyComponent,
    MentorToManagerComponent,
    TraineeToManagerComponent,
    MentorResourceAllocationComponent,
    TraineeToMentorComponent,
    GroupAllocationComponent,
    TraineeToGroupComponent,
    MentorDelConfirmationComponent,
    AllocateResourceComponent,
    ManagerCreateComponent,
    ManagerUpdateComponent,
    BreadcrumbComponent,
    HeaderComponent,
    SidebarComponent,
    DashboardComponent,
    UserFormComponent,
    UserListComponent,
    IndexComponent,
    ImportExportComponent,
    FiltersComponent,
    TrainingFormComponent,
    TechnologyListComponent,
    AllocateResourcesComponent,
    ResourceAllocationComponent,
    GroupResourceAllocationComponent,
    SessionListComponent,
    SessionFormComponent,
    DateTimePickerComponent,
    TaskListComponent,
    TraineeSessionListComponent,
    TaskFormComponent,
    TraineeTaskListComponent,
    ManageTaskComponent,
    TraineeValuationComponent,
    SubtaskManagementComponent,
    SubtaskFormComponent,
    TraineeSubtaskListComponent,
  ],
  imports: [
    BrowserModule,
    NgbModule,
    AutocompleteLibModule,
    HttpClientModule,
    AppRoutingModule,
    ToastrModule.forRoot({
      timeOut: 2500,
      positionClass: 'toast-bottom-right',
      preventDuplicates: true,
      closeButton: false
    }),
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    NgxPaginationModule,
    NgbAccordionModule,
    CommonModule,
    DropzoneModule,
    NgxSkeletonLoaderModule,
    BsDatepickerModule.forRoot(),
    NgbDatepickerModule,
    NgbDatepickerModule, NgbAlertModule, FormsModule, JsonPipe
  ],

  providers: [
    DatePipe,
    {
    provide:HTTP_INTERCEPTORS,
    useClass:BuddyInterceptorInterceptor,
    multi:true
  }],
  bootstrap: [AppComponent]
})
export class AppModule { }
