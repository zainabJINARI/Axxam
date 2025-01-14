import { Component } from '@angular/core';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrl: './edit-profile.component.css',
})
export class EditProfileComponent {
  fileName: string = 'Upload new photo';

  updateFileName(event: any): void {
    const inputFile = event.target;
    // Vérifier si un fichier a été sélectionné
    this.fileName = inputFile.files.length
      ? inputFile.files[0].name
      : 'Aucun fichier sélectionné';
  }
}
