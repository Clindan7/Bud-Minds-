import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { BatchService } from './batch.service';
import { AppConstants } from 'src/app/app.constants';

describe('BatchService', () => {
  let service: BatchService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [BatchService]
    });
    service = TestBed.inject(BatchService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch all batches', () => {
    const mockResponse = [{id: 1, name: 'Batch 1'}, {id: 2, name: 'Batch 2'}];
    service.getBatch().subscribe(response => {
      expect(response).toEqual(mockResponse);
    });
    const request = httpMock.expectOne(`${AppConstants.API_ENDPOINT}batch/all`);
    expect(request.request.method).toBe('GET');
    request.flush(mockResponse);
  });

  it('should fetch batch names', () => {
    const mockResponse = ['Batch 1', 'Batch 2'];
    service.fetchBatchNames().subscribe(response => {
      expect(response).toEqual(mockResponse);
    });
    const request = httpMock.expectOne(`${AppConstants.API_ENDPOINT}batch/batchNames`);
    expect(request.request.method).toBe('GET');
    request.flush(mockResponse);
  });
});

