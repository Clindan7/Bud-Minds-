import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { MentorServiceService } from './mentor-service.service';
import { AppConstants } from 'src/app/app.constants';

describe('MentorServiceService', () => {
  let service: MentorServiceService;
  let httpMock: HttpTestingController;
  const API_ENDPOINT = AppConstants.API_ENDPOINT;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [MentorServiceService]
    });

    service = TestBed.inject(MentorServiceService);
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
    service.fetchUser().subscribe(response => {
      expect(response).toEqual(mockResponse);
    });
    const request = httpMock.expectOne(`${API_ENDPOINT}users/profile`);
    expect(request.request.method).toBe('GET');
    request.flush(mockResponse);
  });

  it('should get mentors without parameters', () => {
    const mockResponse = { mentors: [{ id: 1, name: 'John Doe' }] };
    service.getMentors(1, 10).subscribe(response => {
      expect(response).toEqual(mockResponse);
    });
    const request = httpMock.expectOne(`${API_ENDPOINT}mentors?&page=1`);
    expect(request.request.method).toBe('GET');
    request.flush(mockResponse);
  });

  it('should get mentors with employeeId parameter', () => {
    const mockResponse = { mentors: [{ id: 1, name: 'John Doe' }] };
    service.getMentors(1, 10, { employeeId: 123 }).subscribe(response => {
      expect(response).toEqual(mockResponse);
    });
    const request = httpMock.expectOne(`${API_ENDPOINT}mentors?&page=1&employeeId=123`);
    expect(request.request.method).toBe('GET');
    request.flush(mockResponse);
  });

  it('should get mentors with name parameter', () => {
    const mockResponse = { mentors: [{ id: 1, name: 'John Doe' }] };
    service.getMentors(1, 10, { name: 'John' }).subscribe(response => {
      expect(response).toEqual(mockResponse);
    });
    const request = httpMock.expectOne(`${API_ENDPOINT}mentors?&page=1&search=John`);
    expect(request.request.method).toBe('GET');
    request.flush(mockResponse);
  });

  it('should search user', () => {
    const mockResponse = { users: [{ id: 1, name: 'John Doe' }] };
    service.searchUser().subscribe(response => {
      expect(response).toEqual(mockResponse);
    });
    const request = httpMock.expectOne(`${API_ENDPOINT}mentors/firstName`);
    expect(request.request.method).toBe('GET');
    request.flush(mockResponse);
  });

  it('should delete mentor', () => {
    const mentorId = 1;
    const mockResponse = { success: true };
    service.deleteMentor(mentorId).subscribe(response => {
      expect(response).toEqual(mockResponse);
    });
    const request = httpMock.expectOne(`${API_ENDPOINT}mentors/${mentorId}`);
    expect(request.request.method).toBe('DELETE');
    request.flush(mockResponse);
  });

  it('should change password', () => {
    const data = {oldPassword: '123', newPassword: '456'};
    service.changePassword(data).subscribe();

    const req = httpMock.expectOne(`${API_ENDPOINT}users/changePassword`);
    expect(req.request.method).toEqual('PUT');
    expect(req.request.body).toEqual(data);
    req.flush({});
  });

  it('should register mentor', () => {
    const data = {name: 'John Doe', email: 'johndoe@example.com'};
    service.registerMentor(data).subscribe();

    const req = httpMock.expectOne(`${API_ENDPOINT}mentors/`);
    expect(req.request.method).toEqual('POST');
    expect(req.request.body).toEqual(data);
    req.flush({});
  });

  it('should edit mentor', () => {
    const data = {name: 'John Doe', email: 'johndoe@example.com'};
    const mentorId = '123';
    service.editMentor(data, mentorId).subscribe();

    const req = httpMock.expectOne(`${API_ENDPOINT}mentors/${mentorId}`);
    expect(req.request.method).toEqual('PUT');
    expect(req.request.body).toEqual(data);
    req.flush({});
  });

  it('should view mentor', () => {
    const mentorId = '123';
    service.viewMentor(mentorId).subscribe();

    const req = httpMock.expectOne(`${API_ENDPOINT}mentors/${mentorId}`);
    expect(req.request.method).toEqual('GET');
    req.flush({});
  });

  it('should import CSV', () => {
    const file = new File([''], 'file.csv', {type: 'text/csv'});
    service.importCSV(file).subscribe();

    const req = httpMock.expectOne(`${API_ENDPOINT}mentor/csvImport`);
    expect(req.request.method).toEqual('POST');
    expect(req.request.body instanceof FormData).toBeTruthy();
    expect(req.request.body.get('file')).toEqual(file);
    req.flush({});
  });

  it('should export CSV', () => {
    service.exportCSV().subscribe();

    const req = httpMock.expectOne(`${API_ENDPOINT}mentor/csvExport/file.csv`);
    expect(req.request.method).toEqual('GET');
    expect(req.request.responseType).toEqual('text');
    req.flush({});
  });

  it('should set the page number', () => {
    const pageNum = 1;

    service.setPageNum(pageNum);

    expect(service.page).toEqual(pageNum);
  });

});
