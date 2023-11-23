import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { ManagerResourceAllocationService } from './manager-resource-allocation.service';
import { AppConstants } from 'src/app/app.constants';

describe('ManagerResourceAllocationService', () => {
  let service: ManagerResourceAllocationService;
  let httpMock: HttpTestingController;
  let routeMock: ActivatedRoute;
  const API_ENDPOINT = AppConstants.API_ENDPOINT;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        ManagerResourceAllocationService,
        {
          provide: ActivatedRoute,
          useValue: { snapshot: { paramMap: { get: () => 'testUserRole' } } }
        }
      ]
    });
    service = TestBed.inject(ManagerResourceAllocationService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get assigned resources for a manager and userRole', () => {
    const managerId = 1;
    const userRole = 'testUserRole';
    const dummyResponse = [{ id: 1, name: 'John Doe' }, { id: 2, name: 'Jane Smith' }];
    service.assignedResources(managerId, userRole).subscribe(res => {
      expect(res).toEqual(dummyResponse);
    });
    const req = httpMock.expectOne(`${API_ENDPOINT}managers/resource-list?&managerId=${managerId}&userRole=${userRole}`);
    expect(req.request.method).toBe('GET');
    req.flush(dummyResponse);
  });

  it('should get total resources for a userRole', () => {
    const userRole = 'testUserRole';
    const dummyResponse = [{ id: 1, name: 'John Doe' }, { id: 2, name: 'Jane Smith' }];
    service.totalResources(userRole).subscribe(res => {
      expect(res).toEqual(dummyResponse);
    });
    const req = httpMock.expectOne(`${API_ENDPOINT}managers/resource-list?&userRole=${userRole}`);
    expect(req.request.method).toBe('GET');
    req.flush(dummyResponse);
  });

  it('should allocate resources for a manager and userRole', () => {
    const allocationMode = 'testAllocationMode';
    const managerId = 1;
    const userRole = 'testUserRole';
    const users = [{ id: 1, name: 'John Doe' }, { id: 2, name: 'Jane Smith' }];
    const dummyResponse = { success: true };
    service.allocateResources(allocationMode, managerId, userRole, users).subscribe(res => {
      expect(res).toEqual(dummyResponse);
    });
    const req = httpMock.expectOne(`${API_ENDPOINT}managers/allocation-control?&allocationMode=${allocationMode}&managerId=${managerId}&userRole=${userRole}`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(users);
    req.flush(dummyResponse);
  });

  it('should make a POST request to deallocate resources', () => {
    const allocationMode = 'testDeallocationMode';
    const managerId = 1;
    const userRole = 'testUserRole';
    const users = [{ id: 1, name: 'John Doe' }, { id: 2, name: 'Jane Smith' }];
    const dummyResponse = { success: true };
    service.deallocateResources(allocationMode, managerId, userRole, users).subscribe((response) => {
      expect(response).toEqual(dummyResponse);
    });
    const req = httpMock.expectOne(`${API_ENDPOINT}managers/allocation-control?&allocationMode=${allocationMode}&managerId=${managerId}&userRole=${userRole}`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(users);
    req.flush(dummyResponse);
  });
});
