import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CvEditor } from './cv-editor';

describe('CvEditor', () => {
  let component: CvEditor;
  let fixture: ComponentFixture<CvEditor>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CvEditor]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CvEditor);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
