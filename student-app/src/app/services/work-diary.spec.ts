import { TestBed } from '@angular/core/testing';

import { WorkDiary } from './work-diary';

describe('WorkDiary', () => {
  let service: WorkDiary;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WorkDiary);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
