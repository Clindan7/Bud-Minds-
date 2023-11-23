import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { AppConstants } from 'src/app/app.constants';
import { TrainerServiceService } from './trainer-service.service';


describe('TrainerServiceService', () => {
  let service: TrainerServiceService;
  let httpMock: HttpTestingController;
  const API_ENDPOINT = AppConstants.API_ENDPOINT;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [TrainerServiceService],
    });
    service = TestBed.inject(TrainerServiceService);
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

  it('should get trainers', () => {
    const mockResponse = { trainers: [{ id: 1, name: 'John Doe' }] };
    service.getTrainers(1, 10).subscribe((response) => {
      expect(response).toEqual(mockResponse);
    });
    const req = httpMock.expectOne(`${API_ENDPOINT}trainers?&page=1`);
    expect(req.request.method).toBe('GET');
    req.flush(mockResponse);
  });

  it('should search trainer by first name', () => {
    const mockResponse = [{ id: 1, name: 'John' }, { id: 2, name: 'Jane' }];
    service.searchUser().subscribe((response) => {
      expect(response).toEqual(mockResponse);
    });
    const req = httpMock.expectOne(`${API_ENDPOINT}trainers/firstName`);
    expect(req.request.method).toBe('GET');
    req.flush(mockResponse);
  });

  it('should delete trainer by id', () => {
    const mockResponse = { message: 'Trainer deleted successfully' };
    service.deleteTrainer(1).subscribe((response) => {
      expect(response).toEqual(mockResponse);
    });
    const req = httpMock.expectOne(`${API_ENDPOINT}trainers/1`);
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

  it('should send a POST request to register a trainer', () => {
    const mockData = { name: 'John Doe', email: 'johndoe@example.com' };
    service.registerTrainer(mockData).subscribe();
    const req = httpMock.expectOne(`${API_ENDPOINT}trainers/`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockData);
    req.flush({});
  });

  it('should send a PUT request to edit a trainer', () => {
    const mockData = { name: 'John Doe', email: 'johndoe@example.com' };
    const mockTrainerId = 123;
    service.editTrainer(mockData, mockTrainerId).subscribe();
    const req = httpMock.expectOne(`${API_ENDPOINT}trainers/${mockTrainerId}`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(mockData);
    req.flush({});
  });

  it('should send a GET request to view a trainer', () => {
    const mockTrainerId = 123;
    service.viewTrainer(mockTrainerId).subscribe();
    const req = httpMock.expectOne(`${API_ENDPOINT}trainers/${mockTrainerId}`);
    expect(req.request.method).toBe('GET');
    req.flush({});
  });

  it('should send a POST request to import CSV', () => {
    const mockFile = new File([''], 'mock.csv');
    service.importCSV(mockFile).subscribe();
    const req = httpMock.expectOne(`${API_ENDPOINT}trainer/csvImport`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body instanceof FormData).toBe(true);
    req.flush({});
  });

  it('should send a GET request to export CSV', () => {
    service.exportCSV().subscribe();
    const req = httpMock.expectOne(`${API_ENDPOINT}trainer/csvExport/file.csv`);
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
