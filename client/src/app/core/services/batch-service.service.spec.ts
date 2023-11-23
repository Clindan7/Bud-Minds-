import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { BatchServiceService } from './batch-service.service';
import { AppConstants } from 'src/app/app.constants';

describe('BatchServiceService', () => {
  let service: BatchServiceService;
  let httpTestingController: HttpTestingController;
  const apiEndpoint = AppConstants.API_ENDPOINT;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [BatchServiceService]
    });
    service = TestBed.inject(BatchServiceService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('fetchUser', () => {
    it('should fetch user profile', () => {
      const mockResponse = { name: 'John', email: 'johndoe@example.com' };

      service.fetchUser().subscribe(response => {
        expect(response).toEqual(mockResponse);
      });

      const req = httpTestingController.expectOne(`${apiEndpoint}users/profile`);
      expect(req.request.method).toBe('GET');
      req.flush(mockResponse);
    });
  });

  describe('getBatches', () => {
    it('should get all batches', () => {
      const mockResponse = { batchName: 'Batch A' };

      service.getBatches().subscribe(response => {
        expect(response).toEqual(mockResponse);
      });

      const req = httpTestingController.expectOne(`${apiEndpoint}batch/batchName`);
      expect(req.request.method).toBe('GET');
      req.flush(mockResponse);
    });
  });

  describe('findBatches', () => {
    it('should return an Observable<any>', () => {
      const mockResponse = { batches: [{ name: 'Batch A' }, { name: 'Batch B' }] };
      const page = 1;
      const limit = 10;

      service.findBatches(page, limit).subscribe(response => {
        expect(response).toEqual(mockResponse);
      });

      const req = httpTestingController.expectOne(`${apiEndpoint}batch?limit=${limit}&page=${page}`);
      expect(req.request.method).toBe('GET');
      req.flush(mockResponse);
    });

    it('should include search parameter in the request if provided', () => {
      const mockResponse = { batches: [{ name: 'Batch A' }] };
      const page = 1;
      const limit = 10;
      const searchParam = { batch: 'Batch A' };

      service.findBatches(page, limit, searchParam).subscribe(response => {
        expect(response).toEqual(mockResponse);
      });

      const req = httpTestingController.expectOne(`${apiEndpoint}batch?limit=${limit}&page=${page}&search=${searchParam.batch}`);
      expect(req.request.method).toBe('GET');
      req.flush(mockResponse);
    });
  });

  describe('viewBatch', () => {
    it('should return an Observable<any>', () => {
      const batchId = 1;
      const mockResponse = { id: batchId, name: 'Batch A' };

      service.viewBatch(batchId).subscribe(response => {
        expect(response).toEqual(mockResponse);
      });

      const req = httpTestingController.expectOne(`${apiEndpoint}batch/${batchId}`);
      expect(req.request.method).toBe('GET');
      req.flush(mockResponse);
    });
  });

  it('should add a batch', () => {
    const batchData = { name: 'Batch 1', capacity: 10 };
    const mockResponse = { id: 1, ...batchData };

    service.addBatch(batchData).subscribe((response) => {
      expect(response).toEqual(mockResponse);
    });

    const req = httpTestingController.expectOne(AppConstants.API_ENDPOINT + 'batch');
    expect(req.request.method).toEqual('POST');
    expect(req.request.body).toEqual(batchData);
    req.flush(mockResponse);
  });

  it('should edit a batch', () => {
    const batchId = 1;
    const batchData = { name: 'Batch 2', capacity: 20 };
    const mockResponse = { id: batchId, ...batchData };

    service.editBatch(batchData, batchId).subscribe((response) => {
      expect(response).toEqual(mockResponse);
    });

    const req = httpTestingController.expectOne(AppConstants.API_ENDPOINT + 'batch/' + batchId);
    expect(req.request.method).toEqual('PUT');
    expect(req.request.body).toEqual(batchData);
    req.flush(mockResponse);
  });

  it('should delete a batch', () => {
    const batchId = 1;

    service.deleteBatch(batchId).subscribe();

    const req = httpTestingController.expectOne(AppConstants.API_ENDPOINT + 'batch/' + batchId);
    expect(req.request.method).toEqual('DELETE');
    req.flush({});
  });
});
