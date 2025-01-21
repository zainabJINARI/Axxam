import { Component, OnInit } from '@angular/core';
import { AnnouncementService } from '../../services/announcement.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { CategoryService } from '../../services/category.service';

@Component({
  selector: 'app-search-page',
  templateUrl: './search-page.component.html',
  styleUrls: ['./search-page.component.css']
})
export class SearchPageComponent implements OnInit {
  results:any = []; // Array to store search results
  totalPages!:number ; // Static value for total pages
  currentPage = 0; // Current page
  resultsPerPage = 8; // Number of results per page
  pagination! : number[]; // Array to store pagination links
  total!:number
  searchForm!: FormGroup;
  categories: any[] = [];
  isSearchActivated:boolean=false


  constructor(
    private fb: FormBuilder,
    private categoryService: CategoryService,
    private annService:AnnouncementService) {}

  ngOnInit(): void {
    this.fetchResults();
    this.searchForm = this.fb.group({
      placeName: [''],
      placeLocation: [''],
      placeCategory: [''],
      price: ['']
    });

    // Fetch categories for the dropdown (if needed)
    this.categoryService.getCategories().subscribe(categories => {
      this.categories = categories;
    });
    
  }

  clearSearch(){
    this.isSearchActivated=false
    this.searchForm.reset('')
    this.currentPage=0
    this.fetchResults()
  }

  performSearch(){
    if (this.searchForm.valid) {
      const formData = this.searchForm.value;

      if(!this.isSearchActivated){
        this.currentPage=0


      }
      formData.page = this.currentPage|| 0; 
      formData.size = this.resultsPerPage|| 5;  
      this.annService.getFilteredAnnouncements(formData).subscribe({
        next:(data:any)=>{
          console.log(data)
          this.results= JSON.parse(JSON.stringify(data)).items
        let result =JSON.parse(JSON.stringify(data)).totalItems/this.resultsPerPage 
        
        if(Math.floor(result)!=result){
          result+=1
          result=Math.floor(result)

        }
        this.totalPages=result
        this.total=JSON.parse(JSON.stringify(data)).totalItems

        },
        error:()=>{

        }
      });
    }

  }

  onSubmit(): void {
  
    this.performSearch()
    this.isSearchActivated = true
   
  }

  // Function to fetch results based on the current page
  fetchResults(): void {
    // const fakeData = this.getFakeData(page);
    // this.results = fakeData;

    this.annService.getAnnouncements(this.currentPage,this.resultsPerPage).subscribe({
      next:(data)=>{
        this.results= JSON.parse(JSON.stringify(data)).items
        let result =JSON.parse(JSON.stringify(data)).totalItems/this.resultsPerPage 
        
        if(Math.floor(result)!=result){
          result+=1
          result=Math.floor(result)

        }
        this.totalPages=result
        this.total=JSON.parse(JSON.stringify(data)).totalItems


      },
      error:()=>{
        console.log('error while trying to get announcement s')
      }
    })

  }

  

 

  // Change the current page and fetch the results for that page
  prevPage() {
    this.currentPage-=1
    if(this.isSearchActivated ){
      this.performSearch()
    }else{
      this.fetchResults()

    }
   
    
    }
    goToPage(page: number) {
      this.currentPage=page
      if(this.isSearchActivated ){
        this.performSearch()
      }else{
        this.fetchResults()
  
      }
    }
    nextPage() {
    this.currentPage+=1
    if(this.isSearchActivated ){
      this.performSearch()
    }else{
      this.fetchResults()

    }
    }
}
