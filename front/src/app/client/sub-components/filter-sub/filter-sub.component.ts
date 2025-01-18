import { Component, OnInit } from '@angular/core';
import { Category } from '../../../models/Category';
import { CategoryService } from '../../../services/category.service';

@Component({
  selector: 'app-filter-sub',
  templateUrl: './filter-sub.component.html',
  styleUrl: './filter-sub.component.css'
})
export class FilterSubComponent implements OnInit {

  categories!:Category[]

  constructor(private categoryServ:CategoryService){}
  ngOnInit(): void {
    this.categoryServ.getCategories().subscribe({
      next:(data)=>{
        this.categories=data

      },
      error:()=>{
        console.log('error while trying to get cateogires')
      }
    })
  }

}
