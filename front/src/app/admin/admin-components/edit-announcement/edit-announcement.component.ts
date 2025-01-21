import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Category } from '../../../models/Category';
import { CategoryService } from '../../../services/category.service';
import { AnnouncementService } from '../../../services/announcement.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-edit-announcement',
  templateUrl: './edit-announcement.component.html',
  styleUrl: './edit-announcement.component.css',
})
export class EditAnnouncementComponent implements OnInit {
  public announceForm!: FormGroup;
  currentStep: number = 1;
  categories!: Category[];
  choosenCategoryId: number = -1;

  id!: string;
  initialValues!: any;

  // services:Service[]=[]

  constructor(
    private fb: FormBuilder,
    private categoryService: CategoryService,
    private annServ: AnnouncementService,
    private routerRet: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.id = this.routerRet.snapshot.paramMap.get('id') || '';
    this.annServ.getAnnouncementById(this.id).subscribe({
      next: (data) => {
        let results = JSON.parse(JSON.stringify(data));
        this.choosenCategoryId = results['category'].idC;
        this.announceForm = this.fb.group({
          title: [results['title'], Validators.required],
          address: [results['address'], Validators.required],
          priceForNight: [
            results['priceForNight'],
            [Validators.required, Validators.min(1)],
          ],
          images: [[]], // Initialize as an empty array
          description: [results['description'], Validators.required],
          services: this.fb.array(this.createService(results['services'])),
        });

        this.initialValues = {
          title: results['title'],
          address: results['address'],
          priceForNight: results['priceForNight'],
          description: results['description'],
          categoryId: this.choosenCategoryId,
        };
      },
      error: (error) => {
        console.log(error);
      },
    });

    this.categoryService.getCategories().subscribe({
      next: (data) => {
        this.categories = data;
      },
    });
  }
  createService(services: any[]): FormGroup[] {
    return services.map((s) => {
      return this.fb.group({
        id: [s.id],
        name: [s.title, Validators.required],
        file: [''],
      });
    });
  }
  createServiceEmpty(): FormGroup {
    return this.fb.group({
      id: [''],
      name: ['', Validators.required],
      file: [''],
    });
  }

  get services(): FormArray {
    return this.announceForm.get('services') as FormArray;
  }

  addServices(): void {
    this.services.push(this.createServiceEmpty());
  }

  deleteService(index: number): void {
    if (this.services.length > 1) {
      const serviceGroup = this.services.at(index) as FormGroup;
      const serviceId = serviceGroup.get('id')?.value;
      if (serviceId != '') {
        this.annServ.deleteService(serviceId).subscribe({
          next: () => {
            console.log('service deleted from db successfully');
            this.services.removeAt(index);
          },
          error: (error) => {
            console.log(error);
          },
        });
      } else {
        this.services.removeAt(index);
      }
    } else {
      alert('Vous devez avoir au moins un service.');
    }
  }

  public setChoosenCategory(id: number): void {
    this.choosenCategoryId = id;
    console.log(id);
  }

  onFileChangeImg(event: any): void {
    const files = Array.from(event.target.files); // Convert FileList to an array
    this.announceForm.patchValue({
      images: files, // Prioritize the 'images' field
    });
  }

  updateAnnonce(): void {
    // Extract the current form values
    const updatedValues: any = {
      title: this.announceForm.get('title')?.value,
      address: this.announceForm.get('address')?.value,
      priceForNight: this.announceForm.get('priceForNight')?.value,
      description: this.announceForm.get('description')?.value,
      categoryId: this.choosenCategoryId,
    };

    // Compare the current values with the initial values
    const hasChanges = Object.keys(this.initialValues).some(
      (key) => updatedValues[key] !== this.initialValues[key]
    );

    // If no changes detected, silently return
    if (!hasChanges && this.announceForm.get('images')?.value.length === 0) {
      console.log('No changes detected in the announcement. Saving silently.');
      this.updateServices();
      this.router.navigateByUrl('/admin/announcements');
      return;
    }

    // Prepare the FormData payload
    const formData = new FormData();
    formData.append('id', this.id);
    formData.append('title', updatedValues.title);
    formData.append('address', updatedValues.address);
    formData.append('priceForNight', updatedValues.priceForNight.toString());
    formData.append('description', updatedValues.description);
    formData.append('categoryId', updatedValues.categoryId.toString());

    // Append images if there are any
    const images = this.announceForm.get('images')?.value || [];
    images.forEach((image: File) => {
      formData.append('images', image);
    });

    // Call the update service method
    this.annServ.updateAnnouncement(formData).subscribe({
      next: () => {
        console.log('Announcement updated successfully.');
        this.updateServices();
        this.router.navigateByUrl('/admin/announcements');
      },
      error: (error) => {
        console.error('Error updating announcement:', error);
      },
    });
  }

  updateServices(): void {
    console.log('updating services ongoing...');
    this.services.controls.forEach((serviceControl: any) => {
      const serviceId = serviceControl.get('id')?.value;
      const serviceTitle = serviceControl.get('name')?.value;
      const serviceFile = serviceControl.get('file')?.value;

      // If the service is new (no ID), create it
      if (!serviceId) {
        const newServiceData = new FormData();
        newServiceData.append('title', serviceTitle);
        newServiceData.append('announcementId', this.id);
        if (serviceFile) {
          newServiceData.append('image', serviceFile);
        }

        this.annServ.createService(newServiceData).subscribe({
          next: () => console.log('New service created successfully.'),
          error: (error) => console.error('Error creating service:', error),
        });
      } else {
        const updateServiceData = new FormData();
        updateServiceData.append('id', serviceId);
        updateServiceData.append('title', serviceTitle);
        updateServiceData.append('announcementId', this.id);
        if (serviceFile) {
          updateServiceData.append('image', serviceFile);
        }

        this.annServ.updateService(updateServiceData).subscribe({
          next: () => console.log('Service updated successfully.'),
          error: (error) => console.error('Error updating service:', error),
        });
      }
    });

    console.log('update completed...');
  }

  onFileChange(event: any, index: number): void {
    const file = event.target.files[0];
    const serviceGroup = this.services.at(index) as FormGroup;
    serviceGroup.patchValue({ file });
  }

  cancelEdit(): void {
    if (this.announceForm.dirty) {
      const confirmed = confirm(
        'You have unsaved changes. Are you sure you want to cancel?'
      );
      if (confirmed) {
        this.announceForm.reset(this.announceForm);
        this.router.navigateByUrl('admin/announcements');
      }
    } else {
      this.router.navigateByUrl('admin/announcements');
    }
  }

  nextStep(): void {
    if (this.currentStep === 1) {
      this.currentStep = 2;
    } else {
      console.log("Formulaire d'étape 1 invalide");
    }
  }

  prevStep(): void {
    this.currentStep = 1;
  }
}
