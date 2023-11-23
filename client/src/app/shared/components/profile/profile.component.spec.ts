import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';

import { ProfileComponent } from './profile.component';

describe('ProfileComponent', () => {
  let component: ProfileComponent;
  let fixture: ComponentFixture<ProfileComponent>;
  let toastrService: jasmine.SpyObj<ToastrService>;

  beforeEach(async () => {
    toastrService = jasmine.createSpyObj<ToastrService>('ToasterService', ['error', 'success']);
    await TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ],
      declarations: [ ProfileComponent ],
      providers: [
        FormBuilder,
        { provide: ToastrService, useValue: toastrService }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

});

