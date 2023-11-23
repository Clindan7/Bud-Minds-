import { Component, Input, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { ManagerServiceService } from 'src/app/core/services/manager-service.service';
import { MentorServiceService } from 'src/app/core/services/mentor-service.service';
import { TraineeServiceService } from 'src/app/core/services/trainee-service.service';
import { TrainerServiceService } from 'src/app/core/services/trainer-service.service';

@Component({
  selector: 'app-import-export',
  templateUrl: './import-export.component.html',
  styleUrls: ['./import-export.component.css']
})
export class ImportExportComponent implements OnInit {

  fileData:any
  user: any;
  isFileAttached: boolean = false;

  @Input() userrole: any;

  constructor(
    public managerService: ManagerServiceService,
    public mentorService: MentorServiceService,
    public trainerService: TrainerServiceService,
    public traineeService: TraineeServiceService,
    public toast: ToastrService,
  ) { }

  ngOnInit(): void {
    this.getCurrentUser();
  }

  getCurrentUser() {
    this.managerService.fetchUser().subscribe({
      next: (resp: any) => {
        this.user = resp;
      },
      error: (err: any) => {
        if (err.error.errorCode == "1912") {
          this.toast.error('User not found', '');
        } else if (err.error.errorCode == "1908") {
          this.toast.error('Invalid token', '');
        } else if (err.error.errorCode == '1909') {
          this.toast.error('Authorization token expired', '');
        }
      },
    });
  }

  onFileSelected(event: any) {
    if (event.target.files.length > 0) {
      this.isFileAttached = true;
    } else {
      this.isFileAttached = false;
    }
    const files = event.target.files;
    this.handleFileUpload(files);
  }

  onFileDropped(event: any) {
    event.preventDefault();
    const files = event.dataTransfer.files;
    this.handleFileUpload(files);
  }

  onDragOver(event: any) {
    event.preventDefault();
  }

  handleFileUpload(files: FileList) {
    const file = files[0];
    switch (this.userrole) {
      case 1:
        this.importCSV(this.managerService, file);
        break;
      case 2:
        this.importCSV(this.mentorService, file);
        break;
      case 3:
        this.importCSV(this.trainerService, file);
        break;
      case 4:
        this.importCSV(this.traineeService, file);
        break;
      default:
        this.toast.error('Invalid user role');
        break;
    }
  }

  importCSV(service: any, file: File) {
    return service.importCSV(file).subscribe(
      (resp: any) => this.onImportSuccess(),
      (err: any) => this.onImportError(err)
    );
  }

  onImportSuccess() {
    this.toast.success('Imported Successfully');
  }

  onImportError(err: any) {
    this.errorFn(err);
  }

  exportCSV() {
    switch (this.userrole) {
      case 1:
        this.exportCSVService(this.managerService, "Manager");
        break;
      case 2:
        this.exportCSVService(this.mentorService, "Mentor");
        break;
      case 3:
        this.exportCSVService(this.trainerService, "Trainer");
        break;
      case 4:
        this.exportCSVService(this.traineeService, "Trainee");
        break;
      default:
        this.toast.error('Invalid user role');
        break;
    }
  }

  exportCSVService(service: any, serviceName: string) {
    service.exportCSV().subscribe({
      next: (resp: any) => {
        const a = document.createElement("a");
        a.href = "data:text/csv," + resp;
        let filename = serviceName;
        a.setAttribute("download", filename + ".csv");
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
      },
      error: (err: any) => {
        if (err.rror.errorCode == "1093") {
          this.toast.error('User has no permission to access this action', '');
        }
      }
    });
  }

  sampleDoc() {
    // Define column names
    const csv = 'First Name,Last Name,Email,Employee ID,Department';

    // Create Blob object from CSV string
    const blob = new Blob([csv], { type: 'text/csv' });

    // Create URL for Blob object and set it as href of a new a element
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;

    // Set filename for download
    a.download = 'demo.csv';

    // Programmatically click the a element to initiate download
    document.body.appendChild(a);
    a.click();

    // Remove the a element and revoke the URL to free up memory
    document.body.removeChild(a);
    window.URL.revokeObjectURL(url);
  }

  errorFn(err: any) {
    if(err.error.errorCode){
        this.toast.error(err.error.errorMessage, '');
    }
  }

}
