import { Injectable } from "@angular/core";
import { environment } from "src/environments/environment";
@Injectable()
export class AppConstants{
  public static API_ENDPOINT = environment.apiUrl;
}
