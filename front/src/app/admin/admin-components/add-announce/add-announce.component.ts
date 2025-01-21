import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CategoryService } from '../../../services/category.service';
import { Category } from '../../../models/Category';
import { AnnouncementService } from '../../../services/announcement.service';
import { Service } from '../../../models/Service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-add-announce',
  templateUrl: './add-announce.component.html',
  styleUrls: ['./add-announce.component.css'],
})
export class AddAnnounceComponent implements OnInit {

  public announceForm!: FormGroup;
  currentStep: number = 1;
  categories!: Category[];
  choosenCategoryId: number = -1;

  constructor(
    private fb: FormBuilder,
    private categoryService: CategoryService,
    private annServ: AnnouncementService,
    private router:Router , 
    private toastrService: ToastrService
  ) {}

  ngOnInit(): void {
    this.announceForm = this.fb.group({
      title: ['', Validators.required],
      address: ['', Validators.required],
      priceForNight: ['', [Validators.required, Validators.min(1)]],
      images: [[]], 
      description: ['', Validators.required],
      services: this.fb.array([this.createService()]),
    });

    this.categoryService.getCategories().subscribe({
      next: (data) => {
        this.categories = data;
      },
    });
  }
  createService(): FormGroup {
    return this.fb.group({
      name: ['', Validators.required],
      file: [''],
    });
  }

  get services(): FormArray {
    return this.announceForm.get('services') as FormArray;
  }

  addServices(): void {
    this.services.push(this.createService());
  }

  deleteService(index: number): void {
    if (this.services.length > 1) {
      this.services.removeAt(index);
    } else {
      alert('Vous devez avoir au moins un service.');
    }
  }

  public setChoosenCategory(id: number): void {
    this.choosenCategoryId = id;
    console.log(id);
  }

  onFileChangeImg(event: any): void {
    const files = Array.from(event.target.files); 
    this.announceForm.patchValue({
      images: files,
    });
  }

  saveAnnonce(): void {
    if (this.announceForm.valid) {
      const formData = new FormData();

      // Append text fields
      formData.append('title', this.announceForm.get('title')?.value);
      formData.append('address', this.announceForm.get('address')?.value);
      formData.append('priceForNight', this.announceForm.get('priceForNight')?.value);
      formData.append('categoryId', this.choosenCategoryId.toString());
      formData.append('description', this.announceForm.get('description')?.value);

      // Append images (files)
      const images: File[] = this.announceForm.get('images')?.value;
      if (images && images.length > 0) {
        images.forEach((image) => {
          formData.append('images', image); // Field name matches API
        });
      }
     
      // Send formData via service
      this.annServ.createAnnouncement(formData).subscribe({
        next: (data) => {
          let id= JSON.parse( JSON.stringify(data))['id']
        
          console.log(id)  
          this.createServices(id)
          this.router.navigateByUrl('/admin/announcements') ;
          this.toastrService.success('Announcement created successfully');
          
        },
        error: (error) => console.error('Error creating announcement:', error),
      });
    } else {
      console.log('Invalid form submission');
    }
  }

  onFileChange(event: any, index: number): void {
    const file = event.target.files[0];
    const serviceGroup = this.services.at(index) as FormGroup;
    serviceGroup.patchValue({ file });
  }

  createServices(announcementId: string): void {
    const services = this.announceForm.get('services')?.value;
  
    // Iterate through the services and send each one individually
    services.forEach((service: any) => {
      const serviceFormData = new FormData();
      serviceFormData.append('title', service.name); // Assuming 'name' is the field for service name
      serviceFormData.append('description', service.description || '');
      serviceFormData.append('announcementId', announcementId); // Attach the announcement ID
  
      if (service.file) {
        serviceFormData.append('image', service.file); // Assuming file is the image
      }
  
      // Send the service creation request
      this.annServ.createService(serviceFormData).subscribe({
        next: (response) => {
          console.log('Service created successfully:', response);
        },
        error: (error) => console.error('Error creating service:', error),
      });
    });
  }


  cancelCreation(): void {
    if (this.announceForm.dirty) { // Vérifie si le formulaire a été modifié
      const confirmed = confirm('You have unsaved changes. Are you sure you want to cancel?');
      if (confirmed) {
        this.router.navigateByUrl('/admin/announcements'); // Redirige vers une autre page
      }
    } else {
      this.router.navigateByUrl('/admin/announcements');
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
