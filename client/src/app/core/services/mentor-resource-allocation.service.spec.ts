import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { MentorResourceAllocationService } from './mentor-resource-allocation.service';
import { AppConstants } from 'src/app/app.constants';
import { RouterModule } from '@angular/router';

describe('MentorResourceAllocationService', () => {
  let service: MentorResourceAllocationService;
  let httpTestingController: HttpTestingController;
  const API_ENDPOINT = AppConstants.API_ENDPOINT;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule,
        RouterModule.forRoot([])],
      providers: [MentorResourceAllocationService]
    });
    service = TestBed.inject(MentorResourceAllocationService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return assigned resources for a mentor', () => {
    const mentorId = '123';
    const mockResponse = [{id: 1, name: 'Resource 1'}, {id: 2, name: 'Resource 2'}];

    service.assignedResources(mentorId).subscribe((response) => {
      expect(response).toEqual(mockResponse);
    });

    const req = httpTestingController.expectOne(`${API_ENDPOINT}mentors/resource-list?mentorId=${mentorId}`);
    expect(req.request.method).toEqual('GET');
    req.flush(mockResponse);
  });

  it('should return unassigned resources', () => {
    const mockResponse = [{id: 1, name: 'Resource 1'}, {id: 2, name: 'Resource 2'}];

    service.unassignedResources().subscribe((response) => {
      expect(response).toEqual(mockResponse);
    });

    const req = httpTestingController.expectOne(`${API_ENDPOINT}mentors/resource-list`);
    expect(req.request.method).toEqual('GET');
    req.flush(mockResponse);
  });

  it('should allocate resources for a mentor', () => {
    const allocationMode = 'add';
    const mentorId = '123';
    const users = [{id: 1, name: 'Resource 1'}, {id: 2, name: 'Resource 2'}];

    service.allocateResources(allocationMode, mentorId, users).subscribe((response) => {
      expect(response).toEqual('Allocation successful');
    });

    const req = httpTestingController.expectOne(`${API_ENDPOINT}mentors/allocation-control?&allocationMode=${allocationMode}&mentorId=${mentorId}`);
    expect(req.request.method).toEqual('POST');
    expect(req.request.body).toEqual(users);
    req.flush('Allocation successful');
  });

  it('should deallocate resources for a mentor', () => {
    const allocationMode = 'remove';
    const mentorId = '123';
    const users = [{id: 1, name: 'Resource 1'}, {id: 2, name: 'Resource 2'}];

    service.deallocateResources(allocationMode, mentorId, users).subscribe((response) => {
      expect(response).toEqual('Deallocation successful');
    });

    const req = httpTestingController.expectOne(`${API_ENDPOINT}mentors/allocation-control?&allocationMode=${allocationMode}&mentorId=${mentorId}`);
    expect(req.request.method).toEqual('POST');
    expect(req.request.body).toEqual(users);
    req.flush('Deallocation successful');
  });
});

