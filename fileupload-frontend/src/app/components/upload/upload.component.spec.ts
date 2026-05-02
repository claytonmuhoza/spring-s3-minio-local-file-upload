import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UploadService } from './upload.component';

describe('UploadService', () => {
  let component: UploadService;
  let fixture: ComponentFixture<UploadService>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UploadService]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UploadService);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
