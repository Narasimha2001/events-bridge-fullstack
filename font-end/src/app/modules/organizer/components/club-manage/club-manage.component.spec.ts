import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClubManageComponent } from './club-manage.component';

describe('ClubManageComponent', () => {
  let component: ClubManageComponent;
  let fixture: ComponentFixture<ClubManageComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ClubManageComponent]
    });
    fixture = TestBed.createComponent(ClubManageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
