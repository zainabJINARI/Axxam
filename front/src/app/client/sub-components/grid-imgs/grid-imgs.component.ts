import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-grid-imgs',
  templateUrl: './grid-imgs.component.html',
  styleUrl: './grid-imgs.component.css'
})
export class GridImgsComponent {
  items = [
    { src: 'house.png' },
    { src: 'house.png' },
    { src: 'house.png' },
    { src: 'house.png' },
   
  ];

  @Input() images!: any;

  public nbrcols = 4;

  constructor() {
    this.updateColumns();
    window.onresize = () => this.updateColumns();
  }

  updateColumns() {
    if (window.innerWidth < 600) {
      this.nbrcols = 1;
    } else if (window.innerWidth < 960) {
      this.nbrcols = 2;
    } else {
      this.nbrcols = 4;
    }
  }

}
