import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

  searchForm!: FormGroup;  // Declare the form group
  categories: string[] = ['Category 1', 'Category 2', 'Category 3']; // Example categories

  // Initialize the form controls in ngOnInit
  ngOnInit(): void {
    this.searchForm = new FormGroup({
      placeName: new FormControl(''),        // Initial empty value for place name
      placeLocation: new FormControl(''),    // Initial empty value for place location
      placeCategory: new FormControl(''),    // Initial empty value for place category
      price: new FormControl('')             // Initial empty value for price
    });
  }

  // Handle form submission
  onSubmit(): void {
    console.log(this.searchForm.value);
  }

  // Clear the form
  onClear(): void {
    this.searchForm.reset();
  }
}
