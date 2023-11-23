import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { ToastrModule, ToastrService } from 'ngx-toastr';
import { UserServiceService } from '../../services/user-service.service';

import { ResetpasswordComponent } from './resetpassword.component';

describe('ResetpasswordComponent', () => {
  let component: ResetpasswordComponent;
  let fixture: ComponentFixture<ResetpasswordComponent>;
  let service: UserServiceService;
  let toastrService: jasmine.SpyObj<ToastrService>;
  let spy: jasmine.Spy;

  beforeEach(async () => {
    toastrService = jasmine.createSpyObj<ToastrService>('ToasterService', ['error', 'success']);
    await TestBed.configureTestingModule({
      imports: [ ReactiveFormsModule,
        RouterTestingModule,
        HttpClientTestingModule,
        ToastrModule.forRoot()
      ],
      providers: [UserServiceService,{ provide: ToastrService, useValue: toastrService }],
      declarations: [ ResetpasswordComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ResetpasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
