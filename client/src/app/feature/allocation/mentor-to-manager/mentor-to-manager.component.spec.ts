import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { NgbNavModule } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';

import { MentorToManagerComponent } from './mentor-to-manager.component';

describe('MentorToManagerComponent', () => {
  let component: MentorToManagerComponent;
  let fixture: ComponentFixture<MentorToManagerComponent>;
  let toastrService: jasmine.SpyObj<ToastrService>;

  beforeEach(async () => {
    const activatedRouteStub = {
      snapshot: {
        params: {
          id: 1
        }
      }
    };
    toastrService = jasmine.createSpyObj<ToastrService>('ToasterService', ['error', 'success']);
    await TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        NgbNavModule],
      declarations: [MentorToManagerComponent],
      providers: [
        { provide: ToastrService, useValue: toastrService },
        { provide: ActivatedRoute, useValue: activatedRouteStub }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(MentorToManagerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
