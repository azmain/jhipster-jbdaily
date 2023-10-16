import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { UserSettingsFormService } from './user-settings-form.service';
import { UserSettingsService } from '../service/user-settings.service';
import { IUserSettings } from '../user-settings.model';

import { UserSettingsUpdateComponent } from './user-settings-update.component';

describe('UserSettings Management Update Component', () => {
  let comp: UserSettingsUpdateComponent;
  let fixture: ComponentFixture<UserSettingsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userSettingsFormService: UserSettingsFormService;
  let userSettingsService: UserSettingsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [UserSettingsUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(UserSettingsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserSettingsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userSettingsFormService = TestBed.inject(UserSettingsFormService);
    userSettingsService = TestBed.inject(UserSettingsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const userSettings: IUserSettings = { id: 456 };

      activatedRoute.data = of({ userSettings });
      comp.ngOnInit();

      expect(comp.userSettings).toEqual(userSettings);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserSettings>>();
      const userSettings = { id: 123 };
      jest.spyOn(userSettingsFormService, 'getUserSettings').mockReturnValue(userSettings);
      jest.spyOn(userSettingsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userSettings });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userSettings }));
      saveSubject.complete();

      // THEN
      expect(userSettingsFormService.getUserSettings).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(userSettingsService.update).toHaveBeenCalledWith(expect.objectContaining(userSettings));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserSettings>>();
      const userSettings = { id: 123 };
      jest.spyOn(userSettingsFormService, 'getUserSettings').mockReturnValue({ id: null });
      jest.spyOn(userSettingsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userSettings: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userSettings }));
      saveSubject.complete();

      // THEN
      expect(userSettingsFormService.getUserSettings).toHaveBeenCalled();
      expect(userSettingsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserSettings>>();
      const userSettings = { id: 123 };
      jest.spyOn(userSettingsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userSettings });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userSettingsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
