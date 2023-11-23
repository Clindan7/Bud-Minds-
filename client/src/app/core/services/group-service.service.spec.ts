import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { GroupServiceService } from './group-service.service';
import { AppConstants } from 'src/app/app.constants';

describe('GroupServiceService', () => {
  let service: GroupServiceService;
  let httpMock: HttpTestingController;
  const API_ENDPOINT = AppConstants.API_ENDPOINT;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [GroupServiceService],
    });
    service = TestBed.inject(GroupServiceService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch user profile', () => {
    const mockResponse = { name: 'John Doe' };
    service.fetchUser().subscribe((res) => {
      expect(res).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(`${API_ENDPOINT}users/profile`);
    expect(req.request.method).toBe('GET');
    req.flush(mockResponse);
  });

  it('should get groups with default page and provided limit', () => {
    const mockResponse = { groups: [{ id: 1, name: 'Group 1' }] };
    const limit = 10;

    service.getGroups(null, limit).subscribe((res) => {
      expect(res).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(`${API_ENDPOINT}group?limit=${limit}&page=1`);
    expect(req.request.method).toBe('GET');
    req.flush(mockResponse);
  });

  it('should get groups with provided page and limit and batch name and group name filters', () => {
    const mockResponse = { groups: [{ id: 1, name: 'Group 1' }] };
    const page = 2;
    const limit = 10;
    const batchName = 'Batch A';
    const groupName = 'Group 1';

    service.getGroups(page, limit, { batchName, groupName }).subscribe((res) => {
      expect(res).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(`${API_ENDPOINT}group?limit=${limit}&page=${page}&joinerBatchId=${batchName}&groupSearch=${groupName}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockResponse);
  });

  it('should search batch', () => {
    const mockResponse = { batches: [{ id: 1, name: 'Batch A' }] };
    service.searchBatch().subscribe((res) => {
      expect(res).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(`${API_ENDPOINT}batch/all`);
    expect(req.request.method).toBe('GET');
    req.flush(mockResponse);
  });

  it('should delete group', () => {
    const groupId = 1;
    const mockResponse = { message: 'Group deleted successfully' };

    service.deleteGroup(groupId).subscribe((res) => {
      expect(res).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(`${API_ENDPOINT}group/${groupId}`);
    expect(req.request.method).toBe('DELETE');
    req.flush(mockResponse);
  });

  it('should change password', () => {
    const data = {oldPassword: 'password1', newPassword: 'password2'};
    service.changePassword(data).subscribe(response => {
      expect(response).toBeTruthy();
    });
    const req = httpMock.expectOne(`${API_ENDPOINT}users/changePassword`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(data);
    req.flush({status: 'success'});
  });

  it('should add group', () => {
    const data = {name: 'Test Group'};
    service.addGroup(data).subscribe(response => {
      expect(response).toBeTruthy();
    });
    const req = httpMock.expectOne(`${API_ENDPOINT}group`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(data);
    req.flush({status: 'success'});
  });

  it('should edit group', () => {
    const data = {name: 'Updated Test Group'};
    const groupId = 123;
    service.editGroup(data, groupId).subscribe(response => {
      expect(response).toBeTruthy();
    });
    const req = httpMock.expectOne(`${API_ENDPOINT}group/${groupId}`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(data);
    req.flush({status: 'success'});
  });

  it('should view group', () => {
    const groupId = 123;
    service.viewGroup(groupId).subscribe(response => {
      expect(response).toBeTruthy();
    });
    const req = httpMock.expectOne(`${API_ENDPOINT}group/${groupId}`);
    expect(req.request.method).toBe('GET');
    req.flush({status: 'success'});
  });
});
