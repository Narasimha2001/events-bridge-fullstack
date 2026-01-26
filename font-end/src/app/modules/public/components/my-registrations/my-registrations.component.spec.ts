import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyRegistrationsComponent } from './my-registrations.component';

describe('MyRegistrationsComponent', () => {
  let component: MyRegistrationsComponent;
  let fixture: ComponentFixture<MyRegistrationsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MyRegistrationsComponent]
    });
    fixture = TestBed.createComponent(MyRegistrationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
