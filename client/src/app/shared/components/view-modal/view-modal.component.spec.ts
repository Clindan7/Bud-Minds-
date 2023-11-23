import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NgbModule, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService, ToastrModule } from 'ngx-toastr';
import { BatchService } from 'src/app/core/services/batch.service';

import { ViewModalComponent } from './view-modal.component';

describe('ViewModalComponent', () => {
  let component: ViewModalComponent;
  let fixture: ComponentFixture<ViewModalComponent>;
  let service: BatchService;
  let toastrService: jasmine.SpyObj<ToastrService>;

  beforeEach(async () => {
    toastrService = jasmine.createSpyObj<ToastrService>('ToasterService', ['error', 'success']);
    await TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        NgbModule,
        ToastrModule.forRoot()
      ],
      providers: [BatchService,
        NgbActiveModal,
        { provide: ToastrService, useValue: toastrService }],
      declarations: [ ViewModalComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
