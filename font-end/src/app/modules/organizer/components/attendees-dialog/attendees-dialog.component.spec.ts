import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AttendeesDialogComponent } from './attendees-dialog.component';

describe('AttendeesDialogComponent', () => {
  let component: AttendeesDialogComponent;
  let fixture: ComponentFixture<AttendeesDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AttendeesDialogComponent]
    });
    fixture = TestBed.createComponent(AttendeesDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
