import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateClubDialogComponent } from './create-club-dialog.component';

describe('CreateClubDialogComponent', () => {
  let component: CreateClubDialogComponent;
  let fixture: ComponentFixture<CreateClubDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CreateClubDialogComponent]
    });
    fixture = TestBed.createComponent(CreateClubDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
