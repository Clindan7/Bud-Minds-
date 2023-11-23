import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { GroupAllocationService } from './group-allocation.service';
import { AppConstants } from 'src/app/app.constants';

describe('GroupAllocationService', () => {
  let service: GroupAllocationService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        GroupAllocationService,
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: {
                get(): string {
                  return '1';
                }
              }
            }
          }
        }
      ]
    });
    service = TestBed.inject(GroupAllocationService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch assigned resources for a group', () => {
    const groupId = 1;
    const mockResponse = [{ id: 1, name: 'Resource 1' }, { id: 2, name: 'Resource 2' }];
    service.assignedResources(groupId).subscribe(response => {
      expect(response).toEqual(mockResponse);
    });
    const request = httpMock.expectOne(`${AppConstants.API_ENDPOINT}group/resource-list?&joinerGroupId=${groupId}`);
    expect(request.request.method).toBe('GET');
    request.flush(mockResponse);
  });

  it('should fetch unassigned resources', () => {
    const mockResponse = [{ id: 1, name: 'Resource 1' }, { id: 2, name: 'Resource 2' }];
    service.unassignedResources().subscribe(response => {
      expect(response).toEqual(mockResponse);
    });
    const request = httpMock.expectOne(`${AppConstants.API_ENDPOINT}group/resource-list`);
    expect(request.request.method).toBe('GET');
    request.flush(mockResponse);
  });

  it('should allocate resources to a group', () => {
    const allocationMode = 'allocate';
    const groupId = 1;
    const userRole = 'admin';
    const users = [{ id: 1, name: 'User 1' }, { id: 2, name: 'User 2' }];
    const mockResponse = { success: true };
    service.allocateResources(allocationMode, groupId, userRole, users).subscribe(response => {
      expect(response).toEqual(mockResponse);
    });
    const request = httpMock.expectOne(`${AppConstants.API_ENDPOINT}group/allocation-control?&allocationMode=${allocationMode}&joinerGroupId=${groupId}&userRole=${userRole}`);
    expect(request.request.method).toBe('POST');
    expect(request.request.body).toEqual(users);
    request.flush(mockResponse);
  });

  it('should deallocate resources', () => {
    const allocationMode = 'deallocate';
    const userRole = 'admin';
    const users = [{ id: 1, name: 'User 1' }, { id: 2, name: 'User 2' }];
    const mockResponse = { success: true };
    service.deallocateResources(allocationMode, userRole, users).subscribe(response => {
      expect(response).toEqual(mockResponse);
    });
    const request = httpMock.expectOne(`${AppConstants.API_ENDPOINT}group/allocation-control?&allocationMode=${allocationMode}&joinerGroupId=&userRole=${userRole}`);
    expect(request.request.method).toBe('POST');
    expect(request.request.body).toEqual(users);
    request.flush(mockResponse);
  });
});
