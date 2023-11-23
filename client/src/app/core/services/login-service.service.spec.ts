import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { LoginServiceService } from './login-service.service';

describe('LoginServiceService', () => {
  let service: LoginServiceService;
  let httpMock: HttpTestingController;
  let router: Router;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule],
      providers: [LoginServiceService]
    });

    service = TestBed.inject(LoginServiceService);
    httpMock = TestBed.inject(HttpTestingController);
    router = TestBed.inject(Router);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should check if user is logged in', () => {
    localStorage.setItem('accessToken', 'testToken');
    expect(service.isLoggedIn()).toBeTrue();
  });

  it('should get access token from local storage', () => {
    localStorage.setItem('accessToken', 'testToken');
    expect(service.getAccessToken()).toBe('testToken');
  });

  it('should save access token to local storage', () => {
    service.saveAccessToken('testToken');
    expect(localStorage.getItem('accessToken')).toBe('testToken');
  });

  it('should get refresh token from local storage', () => {
    localStorage.setItem('refreshToken', 'testToken');
    expect(service.getRefreshToken()).toBe('testToken');
  });

  it('should save refresh token to local storage', () => {
    service.saveRefreshToken('testToken');
    expect(localStorage.getItem('refreshToken')).toBe('testToken');
  });

  it('should refresh login and return observable', () => {
    const mockResponse = { accessToken: 'testToken' };
    service.refreshLogin().subscribe(response => {
      expect(response).toEqual(mockResponse);
    });

    const req = httpMock.expectOne('http://localhost:8080/login');
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toBe('testToken');

    req.flush(mockResponse);
  });

  it('should login and save tokens to local storage', () => {
    const mockResponse = {
      userId: 1,
      accessToken: { value: 'testToken' },
      refreshToken: { value: 'testRefreshToken' },
      userRole: 'admin'
    };

    service.userLogin({ email: 'test@test.com', password: 'test' }).subscribe(response => {
      expect(response).toEqual(mockResponse);
      expect(localStorage.getItem('userId')).toBe(btoa('1'));
      expect(localStorage.getItem('accessToken')).toBe('testToken');
      expect(localStorage.getItem('refreshToken')).toBe('testRefreshToken');
      expect(localStorage.getItem('userRole')).toBe(btoa('admin'));
    });

    const req = httpMock.expectOne('http://localhost:8080/login');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ email: 'test@test.com', password: 'test' });

    req.flush(mockResponse);
  });

  it('should clear local storage on logout and navigate to login', () => {
    spyOn(router, 'navigateByUrl');
    localStorage.setItem('accessToken', 'testToken');
    service.logout();
    expect(localStorage.getItem('accessToken')).toBeNull();
    expect(router.navigateByUrl).toHaveBeenCalledWith('/login');
  });
});
