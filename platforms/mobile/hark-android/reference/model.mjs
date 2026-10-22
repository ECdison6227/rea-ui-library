// Independent target behavior. This reducer does not claim original Hark semantics.
export function initialState(){return {draft:'',phase:'empty',operation:0,messages:[],error:null,details:false,done:false};}
export function reduce(state,event){
 switch(event.type){
  case 'input': return state.phase==='sending'?state:{...state,draft:String(event.value).slice(0,2000),phase:String(event.value).trim()?'editing':'empty',error:null};
  case 'submit':case 'retry': return !state.draft.trim()||state.phase==='sending'?state:{...state,phase:'sending',operation:state.operation+1,error:null};
  case 'cancel': return state.phase!=='sending'?state:{...state,phase:state.draft.trim()?'editing':'empty',operation:state.operation+1,error:null};
  case 'result': if(state.phase!=='sending'||event.operation!==state.operation)return state;return event.ok?{...state,phase:'empty',draft:'',messages:[...state.messages,{text:state.draft}],error:null}:{...state,phase:'error',error:'The local demo did not complete. Your draft is still here.'};
  case 'details':return {...state,details:!state.details};
  case 'complete':return {...state,done:!state.done};
  default:return state;
 }
}
