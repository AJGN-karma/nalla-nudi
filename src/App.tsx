/**
 * @license
 * SPDX-License-Identifier: Apache-2.0
 */

import { Search, Star, BookOpen, Volume2, Smartphone } from 'lucide-react';

export default function App() {
  return (
    <div className="min-h-screen bg-slate-50 font-sans text-slate-900 flex flex-col items-center justify-center p-6 text-center">
      <div className="max-w-2xl bg-white rounded-3xl shadow-xl p-12 border border-slate-200">
        <div className="flex justify-center mb-6">
          <div className="p-4 bg-indigo-600 rounded-2xl shadow-lg shadow-indigo-200">
            <BookOpen className="w-12 h-12 text-white" />
          </div>
        </div>
        
        <h1 className="text-4xl font-black tracking-tight text-slate-900">Nalla-Nudi</h1>
        <p className="text-indigo-600 font-bold uppercase tracking-widest text-sm mt-2">Technical Bridge Dictionary</p>
        
        <div className="mt-8 space-y-4 text-left border-t border-slate-100 pt-8">
          <p className="text-slate-600 leading-relaxed text-lg">
            A complete, production-ready **Android Application** has been generated in the project source.
          </p>
          
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mt-6">
            <div className="flex items-center space-x-3 p-3 bg-slate-50 rounded-lg border border-slate-100">
              <Smartphone className="text-indigo-600 w-5 h-5" />
              <span className="font-medium text-slate-700">Native Android (Kotlin)</span>
            </div>
            <div className="flex items-center space-x-3 p-3 bg-slate-50 rounded-lg border border-slate-100">
              <Search className="text-indigo-600 w-5 h-5" />
              <span className="font-medium text-slate-700">Offline-First (Room DB)</span>
            </div>
            <div className="flex items-center space-x-3 p-3 bg-slate-50 rounded-lg border border-slate-100">
              <Volume2 className="text-indigo-600 w-5 h-5" />
              <span className="font-medium text-slate-700">TTS Integration</span>
            </div>
            <div className="flex items-center space-x-3 p-3 bg-slate-50 rounded-lg border border-slate-100">
              <Star className="text-indigo-600 w-5 h-5" />
              <span className="font-medium text-slate-700">Favorites & Flashcards</span>
            </div>
          </div>
        </div>

        <div className="mt-10 p-4 bg-indigo-50 rounded-xl text-indigo-700 text-sm font-medium">
          Ready for export! Open the project in **Android Studio** to build and run.
        </div>
      </div>
      
      <footer className="mt-8 text-slate-400 text-xs uppercase tracking-widest font-semibold flex items-center space-x-2">
        <div className="w-2 h-2 bg-green-500 rounded-full animate-pulse"></div>
        <span>Production Android Bundle Generated</span>
      </footer>
    </div>
  );
}
