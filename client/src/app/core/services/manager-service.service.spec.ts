import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { RouterModule } from '@angular/router';
import { ManagerServiceService } from './manager-service.service';
import { AppConstants } from 'src/app/app.constants';

describe('ManagerServiceService', () => {
  let service: ManagerServiceService;
  let httpMock: HttpTestingController;
  const API_ENDPOINT = AppConstants.API_ENDPOINT;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule,
        RouterModule.forRoot([])],
      providers: [ManagerServiceService]
    });

    service = TestBed.inject(ManagerServiceService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch user data', () => {
    const mockUserData = { name: 'John Doe' };

    service.fetchUser().subscribe(data => {
      expect(data).toEqual(mockUserData);
    });

    const req = httpMock.expectOne(`${API_ENDPOINT}users/profile`);
    expect(req.request.method).toBe('GET');
    req.flush(mockUserData);
  });

  it('should get managers with page and limit', () => {
    const mockPage = 1;
    const mockLimit = 10;
    const mockParams = { employeeId: '123', name: 'John' };
    const mockManagers = [{ id: 1, name: 'John Doe' }, { id: 2, name: 'Jane Doe' }];

    service.listUsers(mockPage, mockLimit, mockParams).subscribe(data => {
      expect(data).toEqual(mockManagers);
    });

    const req = httpMock.expectOne(`${API_ENDPOINT}managers?&page=${mockPage}&employeeId=${mockParams.employeeId}&search=${mockParams.name}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockManagers);
  });

  it('should search for a user', () => {
    const mockUserSearchResult = [{ id: 1, firstName: 'John' }, { id: 2, firstName: 'Jane' }];

    service.searchUser().subscribe(data => {
      expect(data).toEqual(mockUserSearchResult);
    });

    const req = httpMock.expectOne(`${API_ENDPOINT}managers/firstName`);
    expect(req.request.method).toBe('GET');
    req.flush(mockUserSearchResult);
  });

  it('should delete a manager', () => {
    const mockManagerId = 1;

    service.delete(mockManagerId).subscribe(data => {
      expect(data).toBeTruthy();
    });

    const req = httpMock.expectOne(`${API_ENDPOINT}managers/` + mockManagerId);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });

  it('should change password', () => {
    const mockPasswordData = { currentPassword: 'oldPass', newPassword: 'newPass' };

    service.changePassword(mockPasswordData).subscribe(data => {
      expect(data).toBeTruthy();
    });

    const req = httpMock.expectOne(`${API_ENDPOINT}users/changePassword`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(mockPasswordData);
    req.flush({});
  });

  it('should send a POST request to register a manager', () => {
    const mockData = { name: 'John Doe', email: 'john.doe@example.com', password: 'password' };

    service.register(mockData).subscribe(response => {
      expect(response).toBeTruthy();
    });

    const request = httpMock.expectOne(`${API_ENDPOINT}managers/`);
    expect(request.request.method).toBe('POST');
    expect(request.request.body).toEqual(mockData);
    request.flush({});
  });

  it('should send a PUT request to edit a manager', () => {
    const mockData = { name: 'John Doe', email: 'john.doe@example.com', password: 'password' };
    const mockManagerId = 123;

    service.update(mockData, mockManagerId).subscribe(response => {
      expect(response).toBeTruthy();
    });

    const request = httpMock.expectOne(`${API_ENDPOINT}managers/${mockManagerId}`);
    expect(request.request.method).toBe('PUT');
    expect(request.request.body).toEqual(mockData);
    request.flush({});
  });

  it('should send a GET request to view a manager', () => {
    const mockManagerId = 123;

    service.view(mockManagerId).subscribe(response => {
      expect(response).toBeTruthy();
    });

    const request = httpMock.expectOne(`${API_ENDPOINT}managers/${mockManagerId}`);
    expect(request.request.method).toBe('GET');
    request.flush({});
  });

  it('should send a POST request to import a CSV file', () => {
    const mockFile = new File([], 'test.csv');
    const mockResponse = { status: 'success' };

    service.importCSV(mockFile).subscribe(response => {
      expect(response).toBeTruthy();
    });

    const request = httpMock.expectOne(`${API_ENDPOINT}manager/csvImport`);
    expect(request.request.method).toBe('POST');
    expect(request.request.body instanceof FormData).toBeTrue();
    request.flush(mockResponse);
  });

  it('should send a GET request to export a CSV file', () => {
    service.exportCSV().subscribe(response => {
      expect(response).toBeTruthy();
    });

    const request = httpMock.expectOne(`${API_ENDPOINT}manager/csvExport/file.csv`);
    expect(request.request.method).toBe('GET');
    request.flush({});
  });

  it('should set the page number', () => {
    const pageNum = 1;

    service.setPageNum(pageNum);

    expect(service.page).toEqual(pageNum);
  });

});
