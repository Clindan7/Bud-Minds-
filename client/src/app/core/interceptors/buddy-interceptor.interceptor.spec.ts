import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { BuddyInterceptorInterceptor } from './buddy-interceptor.interceptor';

describe('BuddyInterceptorInterceptor', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [
      HttpClientTestingModule
    ],
    providers: [
      BuddyInterceptorInterceptor
      ]
  }));

  it('should be created', () => {
    const interceptor: BuddyInterceptorInterceptor = TestBed.inject(BuddyInterceptorInterceptor);
    expect(interceptor).toBeTruthy();
  });
});
