import { Category } from "./Category";
import { Reaction } from "./Review";
import { Service } from "./Service";
import { User } from "./User";

export interface Announcement {
    id?: string; 
    title: string;
    description: string;
    address: string;
    priceForNight: number;
    dateCreation?: Date; 
    category?: Category; 
    host?: User; 
    services?: Service[]; 
    photos?: string[]; 

    review?:Reaction[];
  }

  