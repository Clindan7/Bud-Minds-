import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TrainingServiceService } from './training-service.service';
import { AppConstants } from 'src/app/app.constants';

describe('TrainingServiceService', () => {
  let service: TrainingServiceService;
  let httpMock: HttpTestingController;
  const API_ENDPOINT = AppConstants.API_ENDPOINT;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [TrainingServiceService]
    });

    service = TestBed.inject(TrainingServiceService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch user', () => {
    const mockResponse = { name: 'John Doe' };

    service.fetchUser().subscribe((response) => {
      expect(response).toEqual(mockResponse);
    });

    const request = httpMock.expectOne(`${API_ENDPOINT}users/profile`);
    expect(request.request.method).toBe('GET');
    request.flush(mockResponse);
  });

  it('should get training', () => {
    const mockResponse = [{ id: 1, name: 'Training 1' }];

    service.getTraining(1, 10).subscribe((response) => {
      expect(response).toEqual(mockResponse);
    });

    const request = httpMock.expectOne(`${API_ENDPOINT}training?limit=10&page=1`);
    expect(request.request.method).toBe('GET');
    request.flush(mockResponse);
  });

  it('should add technology', () => {
    const mockData = { name: 'Technology 1' };
    const mockResponse = { id: 1, ...mockData };

    service.addTechnology(mockData).subscribe((response) => {
      expect(response).toEqual(mockResponse);
    });

    const request = httpMock.expectOne(`${API_ENDPOINT}technology/`);
    expect(request.request.method).toBe('POST');
    request.flush(mockResponse);
  });

  it('should delete a technology', () => {
    const technologyId = 1;
    service.deleteTechnology(technologyId).subscribe((response) => {
      expect(response).toBeTruthy();
    });
    const req = httpMock.expectOne(`${API_ENDPOINT}technology/` + technologyId);
    expect(req.request.method).toEqual('DELETE');
    req.flush({ status: 'success' });
  });

  it('should delete a training', () => {
    const trainingId = 1;
    service.deleteTraining(trainingId).subscribe((response) => {
      expect(response).toBeTruthy();
    });
    const req = httpMock.expectOne(`${API_ENDPOINT}training/` + trainingId);
    expect(req.request.method).toEqual('DELETE');
    req.flush({ status: 'success' });
  });

  it('should change password', () => {
    const data = { oldPassword: 'password', newPassword: 'newpassword' };
    service.changePassword(data).subscribe((response) => {
      expect(response).toBeTruthy();
    });
    const req = httpMock.expectOne(`${API_ENDPOINT}users/changePassword`);
    expect(req.request.method).toEqual('PUT');
    req.flush({ status: 'success' });
  });

  it('should add a training', () => {
    const training = { name: 'Angular training', technology: 'Angular' };
    service.addTraining(training).subscribe((response) => {
      expect(response).toBeTruthy();
    });
    const req = httpMock.expectOne(`${API_ENDPOINT}training/`);
    expect(req.request.method).toEqual('POST');
    req.flush(training);
  });

  it('should edit a training', () => {
    const trainingId = 1;
    const training = { name: 'React training', technology: 'React' };
    service.editTraining(training, trainingId).subscribe((response) => {
      expect(response).toBeTruthy();
    });
    const req = httpMock.expectOne(`${API_ENDPOINT}training/` + trainingId);
    expect(req.request.method).toEqual('PUT');
    req.flush(training);
  });

  it('should view a training', () => {
    const trainingId = 1;
    const training = { name: 'React training', technology: 'React' };
    service.viewTraining(trainingId).subscribe((response) => {
      expect(response).toBeTruthy();
    });
    const req = httpMock.expectOne(`${API_ENDPOINT}training/${trainingId}`);
    expect(req.request.method).toEqual('GET');
    req.flush(training);
  });

  it('should set the page number', () => {
    const mockPageNum = 1;
    service.setPageNum(mockPageNum);
    expect(service.page).toBe(mockPageNum);
  });

});
