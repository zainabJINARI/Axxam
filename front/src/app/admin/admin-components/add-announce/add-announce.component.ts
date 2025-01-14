import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormArray, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-add-announce',
  templateUrl: './add-announce.component.html',
  styleUrls: ['./add-announce.component.css'],
})
export class AddAnnounceComponent implements OnInit {
  public announceForm!: FormGroup;
  currentStep: number = 2;
  categories = [
    'Chambre',
    'Salon',
    'Cuisine',
    'Jardin',
    'Chambre',
    'Salon',
    'Cuisine',
    'Jardin',
  ];

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.announceForm = this.fb.group({
      title: ['', Validators.required],
      address: ['', Validators.required],
      price: ['', [Validators.required, Validators.min(1)]],
      photos: [''],
      description: ['', Validators.required],
      services: this.fb.array([this.createService()]),
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

  onFileChange(event: any, index: number): void {
    const file = event.target.files[0];
    if (file) {
      this.services.at(index).patchValue({
        file: file,
      });
    }
  }

  saveAnnonce(): void {
    if (this.announceForm.valid) {
      console.log('Formulaire soumis:', this.announceForm.value);
    } else {
      console.log('Formulaire invalide');
    }
  }

  nextStep(): void {
    // if (this.currentStep === 1 && this.announceForm.valid) {
    //   this.currentStep = 2;
    // }
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
