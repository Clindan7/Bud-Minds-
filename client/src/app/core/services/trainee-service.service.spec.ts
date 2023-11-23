import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { AppConstants } from 'src/app/app.constants';
import { TraineeServiceService } from './trainee-service.service';

describe('TraineeServiceService', () => {
  let service: TraineeServiceService;
  let httpMock: HttpTestingController;
  const API_ENDPOINT = AppConstants.API_ENDPOINT;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [TraineeServiceService],
    });
    service = TestBed.inject(TraineeServiceService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch user profile', () => {
    const mockResponse = { id: 1, name: 'John Doe' };
    service.fetchUser().subscribe((response) => {
      expect(response).toEqual(mockResponse);
    });
    const req = httpMock.expectOne(`${API_ENDPOINT}users/profile`);
    expect(req.request.method).toBe('GET');
    req.flush(mockResponse);
  });

  it('should get trainees', () => {
    const mockResponse = { trainees: [{ id: 1, name: 'John Doe' }] };
    service.getTrainees(1, 10).subscribe((response) => {
      expect(response).toEqual(mockResponse);
    });
    const req = httpMock.expectOne(`${API_ENDPOINT}trainees?&page=1`);
    expect(req.request.method).toBe('GET');
    req.flush(mockResponse);
  });

  it('should search trainee by first name', () => {
    const mockResponse = [{ id: 1, name: 'John' }, { id: 2, name: 'Jane' }];
    service.searchTrainee().subscribe((response) => {
      expect(response).toEqual(mockResponse);
    });
    const req = httpMock.expectOne(`${API_ENDPOINT}trainees/firstName`);
    expect(req.request.method).toBe('GET');
    req.flush(mockResponse);
  });

  it('should delete trainee by id', () => {
    const mockResponse = { message: 'Trainee deleted successfully' };
    service.deleteTrainee(1).subscribe((response) => {
      expect(response).toEqual(mockResponse);
    });
    const req = httpMock.expectOne(`${API_ENDPOINT}trainees/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush(mockResponse);
  });

  it('should send a PUT request to change password', () => {
    const mockData = { oldPassword: 'password', newPassword: 'newpassword' };
    service.changePassword(mockData).subscribe();
    const req = httpMock.expectOne(`${API_ENDPOINT}users/changePassword`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(mockData);
    req.flush({});
  });

  it('should send a POST request to register a trainee', () => {
    const mockData = { name: 'John Doe', email: 'johndoe@example.com' };
    service.registerTrainee(mockData).subscribe();
    const req = httpMock.expectOne(`${API_ENDPOINT}trainees/`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockData);
    req.flush({});
  });

  it('should send a PUT request to edit a trainee', () => {
    const mockData = { name: 'John Doe', email: 'johndoe@example.com' };
    const mockTraineeId = 123;
    service.editTrainee(mockData, mockTraineeId).subscribe();
    const req = httpMock.expectOne(`${API_ENDPOINT}trainees/${mockTraineeId}`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(mockData);
    req.flush({});
  });

  it('should send a GET request to view a trainee', () => {
    const mockTraineeId = 123;
    service.viewTrainee(mockTraineeId).subscribe();
    const req = httpMock.expectOne(`${API_ENDPOINT}trainees/${mockTraineeId}`);
    expect(req.request.method).toBe('GET');
    req.flush({});
  });

  it('should send a POST request to import CSV', () => {
    const mockFile = new File([''], 'mock.csv');
    service.importCSV(mockFile).subscribe();
    const req = httpMock.expectOne(`${API_ENDPOINT}trainee/csvImport`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body instanceof FormData).toBe(true);
    req.flush({});
  });

  it('should send a GET request to export CSV', () => {
    service.exportCSV().subscribe();
    const req = httpMock.expectOne(`${API_ENDPOINT}trainee/csvExport/file.csv`);
    expect(req.request.method).toBe('GET');
    expect(req.request.responseType).toBe('text');
    req.flush({});
  });

  it('should set the page number', () => {
    const mockPageNum = 1;
    service.setPageNum(mockPageNum);
    expect(service.page).toBe(mockPageNum);
  });

});
