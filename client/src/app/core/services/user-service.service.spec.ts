import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { AppConstants } from 'src/app/app.constants';
import { UserServiceService } from './user-service.service';

describe('UserServiceService', () => {
  let service: UserServiceService;
  let httpMock: HttpTestingController;
  const API_ENDPOINT = AppConstants.API_ENDPOINT;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [UserServiceService]
    });
    service = TestBed.inject(UserServiceService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch user profile', () => {
    const mockUser = {name: 'John Doe'};
    const expectedUrl = `${API_ENDPOINT}users/profile`;
    service.fetchUser().subscribe(response => {
      expect(response).toBeTruthy();
      expect(response).toEqual(mockUser);
    });
    const req = httpMock.expectOne(expectedUrl);
    expect(req.request.method).toBe('GET');
    req.flush(mockUser);
  });

  it('should update user profile', () => {
    const mockForm = {name: 'Jane Doe'};
    const expectedUrl = `${API_ENDPOINT}users/profile`;
    service.updateProfile(mockForm).subscribe(response => {
      expect(response).toBeTruthy();
      expect(response).toEqual(mockForm);
    });
    const req = httpMock.expectOne(expectedUrl);
    expect(req.request.method).toBe('PUT');
    req.flush(mockForm);
  });

  it('should make a POST request to forgot password', () => {
    const mockData = {email: 'john@example.com'};
    const expectedUrl = `${API_ENDPOINT}users/forgotPassword`;
    service.forgotPassword(mockData).subscribe(response => {
      expect(response).toBeTruthy();
    });
    const req = httpMock.expectOne(expectedUrl);
    expect(req.request.method).toBe('POST');
    req.flush({});
  });

  it('should make a PUT request to reset password', () => {
    const mockData = {password: 'newPassword'};
    const expectedUrl = `${API_ENDPOINT}users/resetPassword`;
    service.resetPassword(mockData).subscribe(response => {
      expect(response).toBeTruthy();
    });
    const req = httpMock.expectOne(expectedUrl);
    expect(req.request.method).toBe('PUT');
    req.flush({});
  });

  it('should make a POST request to validate token', () => {
    const mockToken = {token: 'abc123'};
    const expectedUrl = `${API_ENDPOINT}users/token/verify`;
    service.validateToken(mockToken).subscribe(response => {
      expect(response).toBeTruthy();
    });
    const req = httpMock.expectOne(expectedUrl);
    expect(req.request.method).toBe('POST');
    req.flush({});
  });

  it('should make a PUT request to change password', () => {
    const mockData = {currentPassword: 'oldPassword', newPassword: 'newPassword'};
    const expectedUrl = `${API_ENDPOINT}users/changePassword`;
    service.changePassword(mockData).subscribe(response => {
      expect(response).toBeTruthy();
    });
    const req = httpMock.expectOne(expectedUrl);
    expect(req.request.method).toBe('PUT');
    req.flush({});
  });
});

