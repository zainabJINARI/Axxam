import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-search-page',
  templateUrl: './search-page.component.html',
  styleUrls: ['./search-page.component.css']
})
export class SearchPageComponent implements OnInit {
  results:any = []; // Array to store search results
  totalPages = 3; // Static value for total pages
  currentPage = 1; // Current page
  resultsPerPage = 8; // Number of results per page
  pagination! : number[]; // Array to store pagination links

  constructor() {}

  ngOnInit(): void {
    this.fetchResults(this.currentPage);
    this.generatePagination();
  }

  // Function to fetch results based on the current page
  fetchResults(page: number): void {
    const fakeData = this.getFakeData(page);
    this.results = fakeData;
  }

  // Fake data generation based on page number
  getFakeData(page: number) {
    const data = [
      [
        { title: 'Place 1', description: 'Description of place 1' },
        { title: 'Place 2', description: 'Description of place 2' },
        { title: 'Place 3', description: 'Description of place 3' },
        { title: 'Place 4', description: 'Description of place 4' },
        { title: 'Place 5', description: 'Description of place 5' },
        { title: 'Place 6', description: 'Description of place 6' },
        { title: 'Place 7', description: 'Description of place 7' },
        { title: 'Place 8', description: 'Description of place 8' }
      ],
      [
        { title: 'Place 9', description: 'Description of place 9' },
        { title: 'Place 10', description: 'Description of place 10' },
        { title: 'Place 11', description: 'Description of place 11' },
        { title: 'Place 12', description: 'Description of place 12' },
        { title: 'Place 13', description: 'Description of place 13' },
        { title: 'Place 14', description: 'Description of place 14' },
        { title: 'Place 15', description: 'Description of place 15' },
        { title: 'Place 16', description: 'Description of place 16' }
      ],
      [
        { title: 'Place 17', description: 'Description of place 17' },
        { title: 'Place 18', description: 'Description of place 18' },
        { title: 'Place 19', description: 'Description of place 19' },
        { title: 'Place 20', description: 'Description of place 20' },
        { title: 'Place 21', description: 'Description of place 21' },
        { title: 'Place 22', description: 'Description of place 22' },
        { title: 'Place 23', description: 'Description of place 23' },
        { title: 'Place 24', description: 'Description of place 24' }
      ]
    ];

    return data[page - 1]; // Return the results for the current page
  }

  // Generate pagination links based on total pages
  generatePagination(): void {
    this.pagination = [];
    for (let i = 1; i <= this.totalPages; i++) {
      this.pagination.push(i);
    }
  }

  // Change the current page and fetch the results for that page
  goToPage(page: number): void {
    this.currentPage = page;
    this.fetchResults(this.currentPage);
  }

  // Simulate navigating to the next page
  nextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.fetchResults(this.currentPage);
    }
  }

  // Simulate navigating to the previous page
  prevPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.fetchResults(this.currentPage);
    }
  }
}
